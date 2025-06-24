import cv2 as cv
import numpy as np
import math
import json

# פונקציה שמחשבת מרחק בין שתי נקודות
def distance(p1, p2):
    return math.sqrt((p1[0] - p2[0])**2 + (p1[1] - p2[1])**2)

# טוענים את התמונה
image_path = 'mountains.jpg'
img = cv.imread(image_path)

if img is None:
    print(f"Error: Could not load image at {image_path}. Please check the path and filename.")
else:
    height, width = img.shape[:2]
    gray = cv.cvtColor(img, cv.COLOR_BGR2GRAY)

    print(f"Loaded image with dimensions: {width}x{height}")

    # שלב 1: זיהוי נקודות חשובות בתמונה

    # מזהים קצוות חזקים (גבולות בין צבעים) בתמונה
    # פרמטרי Canny נשארו כפי שהיו כדי לשמור על קצוות טובים בהרים
    edges = cv.Canny(gray, 30, 90)
    contours, _ = cv.findContours(edges, cv.RETR_LIST, cv.CHAIN_APPROX_SIMPLE)
    edge_points = []
    for contour in contours:
        epsilon = 0.003 * cv.arcLength(contour, True)
        approx = cv.approxPolyDP(contour, epsilon, True)
        for point in approx:
            edge_points.append(tuple(point[0]))

    # מזהים פינות חזקות (כמו קודקודים של צורות)
    # הגדלנו את maxCorners ואת qualityLevel כדי לזהות יותר פינות, שיעזרו לפרטי ההרים
    # minDistance קטן יותר כדי לאפשר לנקודות להיות קרובות יותר, מה שיוסיף צפיפות
    corners = cv.goodFeaturesToTrack(gray, maxCorners=4000, qualityLevel=0.001, minDistance=2) # <<-- שינוי: יותר פינות, איכות נמוכה יותר, מרחק מינימלי קטן יותר
    corner_points = []
    if corners is not None:
        for corner in corners:
            x, y = corner.ravel()
            corner_points.append((int(x), int(y)))

    # מאחדים את כל הנקודות שמצאנו ומסירים כפילויות
    all_raw_points = list(set(edge_points + corner_points))

    # מוסיפים ידנית נקודות חשובות בקצוות ובמרכזי התמונה
    all_raw_points.extend([(0,0), (width-1,0), (0,height-1), (width-1,height-1)])
    all_raw_points.extend([(width//2, 0), (0, height//2), (width-1, height//2), (width//2, height-1)])
    all_raw_points.extend([(width//4, 0), (width*3//4, 0), (0, height//4), (0, height*3//4)])
    all_raw_points.extend([(width-1, height//4), (width-1, height*3//4), (width//4, height-1), (width*3//4, height-1)])


    #  הוספת נקודות אקראיות – עליון עם צפיפות גבוהה, אמצעי עם צפיפות בינונית, תחתון עם צפיפות גבוהה מאוד
    top_height_limit = height // 3 # סף גובה לשליש העליון (שמיים)
    middle_height_limit = height * 2 // 3 # סף גובה לשליש האמצעי (מעבר בין שמיים להרים)
    # שליש תחתון (הרים ועצים) מתחיל מ-middle_height_limit ועד height-1

    num_top_points = 800 # מספר נקודות אקראיות לשמיים (שליש עליון). כדי שהשמיים יהיו חלקים.
    num_middle_points = 400 # מספר נקודות אקראיות לאמצע.
    num_bottom_points = 5000 #  מספר גבוה מאוד של נקודות לחלק התחתון (הרים ועצים)

    # נקודות בשליש העליון (שמיים - צפיפות נמוכה)
    for _ in range(num_top_points):
        x = np.random.randint(0, width)
        y = np.random.randint(0, top_height_limit)
        all_raw_points.append((x, y))

    # נקודות בשליש האמצעי (מעבר - צפיפות בינונית)
    for _ in range(num_middle_points):
        x = np.random.randint(0, width)
        y = np.random.randint(top_height_limit, middle_height_limit)
        all_raw_points.append((x, y))

    # נקודות בשליש התחתון (הרים/עצים - צפיפות גבוהה מאוד)
    for _ in range(num_bottom_points): # <<-- הוספה: לולאה להוספת נקודות בחלק התחתון
        x = np.random.randint(0, width)
        y = np.random.randint(middle_height_limit, height)
        all_raw_points.append((x, y))

    # שלב 2: סינון נקודות קרובות מדי
    # עם הוספת נקודות אקראיות, min_point_distance הופך לקריטי מאוד
    # ערך נמוך יותר יאפשר ליותר נקודות להישאר, מה שיוביל ליותר משולשים
    min_point_distance = 4 #  הקטנה של המרחק המינימלי כדי לשמר יותר פרטים
    filtered_points = []
    all_raw_points.sort(key=lambda p: (p[1], p[0]))

    # שלב סינון: מסנן את כל הנקודות כך שלא תהיינה קרובות מדי זו לזו (מונע הצפה של משולשים קטנים)
    for p in all_raw_points:
        if all(distance(p, existing_p) >= min_point_distance for existing_p in filtered_points):
            filtered_points.append(p)

    # ממיר את רשימת הנקודות למערך numpy בפורמט float32, מוכן לטריאנגולציה
    points_for_subdiv = np.array(filtered_points, dtype=np.float32).reshape(-1, 2)
    print(f"Filtered down to {len(points_for_subdiv)} points for triangulation.")

    if len(points_for_subdiv) < 3:
        print("Not enough unique points detected to form triangles. Adjust parameters.")
    else:
        rect = (0, 0, img.shape[1], img.shape[0])
        # יוצר את מנוע הטריאנגולציה של OpenCV
        subdiv = cv.Subdiv2D(rect)

        # מוסיף את כל הנקודות לטריאנגולציה, רק אם הן בגבולות התמונה
        for p in points_for_subdiv:
            if 0 <= p[0] < width and 0 <= p[1] < height:
                subdiv.insert(tuple(p))

         # שולף את רשימת המשולשים שנוצרו (כל משולש – 6 ערכים: x1, y1, x2, y2, x3, y3)
        triangle_list = subdiv.getTriangleList()

        # יוצרים עותקים של התמונה המקורית: אחד לציור קווי המשולשים, אחד למילוי בצבעים
        img_triangles = img.copy()
        img_filled_triangles = img.copy()
        all_triangles_data = []
        min_area_threshold = 5
        margin_x, margin_y = 10, 10

        # עובר על כל המשולשים שנוצרו
        for t in triangle_list:
            pt1_2d = (int(t[0]), int(t[1]))
            pt2_2d = (int(t[2]), int(t[3]))
            pt3_2d = (int(t[4]), int(t[5]))

            # בודק שכל הקודקודים בטווח התמונה (כולל מרווח)
            if all(
                -margin_x <= pt[0] < img.shape[1] + margin_x and
                -margin_y <= pt[1] < img.shape[0] + margin_y
                for pt in [pt1_2d, pt2_2d, pt3_2d]
            ):
                # מחשב את שטח המשולש (נוסחת דטרמיננטה)
                area = 0.5 * abs(
                    pt1_2d[0]*(pt2_2d[1] - pt3_2d[1]) +
                    pt2_2d[0]*(pt3_2d[1] - pt1_2d[1]) +
                    pt3_2d[0]*(pt1_2d[1] - pt2_2d[1])
                )

                # מדלג על משולשים קטנים מדי
                if area > min_area_threshold:
                    # יוצר מערך numpy של קודקודי המשולש, נדרש לפונקציות של OpenCV
                    triangle_pts_2d_np = np.array([pt1_2d, pt2_2d, pt3_2d], np.int32)
                    # יוצר מסיכה בגודל התמונה, עליה נמלא את המשולש בלבן (255)
                    mask = np.zeros(img.shape[:2], dtype=np.uint8)
                    cv.fillPoly(mask, [triangle_pts_2d_np], 255)

                    if np.any(mask == 255):
                        # לוקח את ערכי הצבע של כל הפיקסלים שבתוך המשולש בלבד,
                        # ע"י שימוש במסיכה: mask==255 מסמן בדיוק את הפיקסלים שנמצאים במשולש,
                        # וכך אפשר לחשב ממוצע צבע רק עבורם בקלות וביעילות.
                        b = img[:, :, 0][mask == 255]
                        g = img[:, :, 1][mask == 255]
                        r = img[:, :, 2][mask == 255]
                        avg_b, avg_g, avg_r = int(np.mean(b)), int(np.mean(g)), int(np.mean(r))
                        avg_rgb = (avg_r, avg_g, avg_b)

                        all_triangles_data.append({
                            "points": [[pt1_2d[0], pt1_2d[1], 0],
                                       [pt2_2d[0], pt2_2d[1], 0],
                                       [pt3_2d[0], pt3_2d[1], 0]],
                            "color_rgb": avg_rgb
                        })

                        cv.line(img_triangles, pt1_2d, pt2_2d, (0, 255, 0), 1)
                        cv.line(img_triangles, pt2_2d, pt3_2d, (0, 255, 0), 1)
                        cv.line(img_triangles, pt3_2d, pt1_2d, (0, 255, 0), 1)

                        # שימו לב: הקו השחור חזר בקוד שלך בתמונה המלאה המקורית ששלחת
                        # אם אתה רוצה מעברים חלקים ללא קווים, הסר או הגב את השורה הבאה
                        cv.fillPoly(img_filled_triangles, [triangle_pts_2d_np], (avg_b, avg_g, avg_r))
                        cv.polylines(img_filled_triangles, [triangle_pts_2d_np], True, (0, 0, 0), 0)

        print(f"Total triangles generated: {len(all_triangles_data)}")

        #  הוספת טשטוש קל לתוצאה הסופית כדי להחליק את קצוות המשולשים
        # אם יש קווים שחורים, הטשטוש הזה יהיה פחות אפקטיבי
        img_filled_triangles = cv.GaussianBlur(img_filled_triangles, (3, 3), 0)


        with open('mountains_data.json', 'w', encoding='utf-8') as f:
            json.dump(all_triangles_data, f, ensure_ascii=False, indent=4)

        with open('mountains_data.txt', 'w', encoding='utf-8') as f:
            for tri in all_triangles_data:
                flat = [str(val) for pt in tri["points"] for val in pt] + [str(c) for c in tri["color_rgb"]]
                f.write(','.join(flat) + '\n')

        cv.imshow('Original Image', img)
        cv.imshow('Detected Edges (Canny)', edges)
        cv.imshow('Triangulated Image (Lines)', img_triangles)
        cv.imshow('Triangulated Image (Filled with Avg Color)', img_filled_triangles)
        cv.waitKey(0)
        cv.destroyAllWindows()