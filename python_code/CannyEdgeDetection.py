import cv2 as cv
import numpy as np
import math
import json

# פונקציה שמחשבת מרחק בין שתי נקודות
def distance(p1, p2):
    return math.sqrt((p1[0] - p2[0])**2 + (p1[1] - p2[1])**2)

# טוענים את התמונה
image_path = 'tiger.jpg'
img = cv.imread(image_path)

if img is None:
    print(f"Error: Could not load image at {image_path}. Please check the path and filename.")
else:
    height, width = img.shape[:2]
    gray = cv.cvtColor(img, cv.COLOR_BGR2GRAY) # ממירים לגווני אפור

    print(f"Loaded image with dimensions: {width}x{height}")

    # שלב 1: זיהוי נקודות חשובות בתמונה

    # מזהים קצוות חזקים (גבולות בין צבעים) בתמונה
    edges = cv.Canny(gray, 50, 150)

    # מוצאים קווי מתאר (קונטורים) מתוך הקצוות ומפשטים אותם לנקודות עיקריות
    contours, _ = cv.findContours(edges, cv.RETR_LIST, cv.CHAIN_APPROX_SIMPLE)
    edge_points = []
    for contour in contours:
        epsilon = 0.003 * cv.arcLength(contour, True)
        approx = cv.approxPolyDP(contour, epsilon, True)
        for point in approx:
            edge_points.append(tuple(point[0]))

    # מזהים פינות חזקות (כמו קודקודים של צורות)
    corners = cv.goodFeaturesToTrack(gray, maxCorners=2000, qualityLevel=0.005, minDistance=3) # יותר פינות, צפופות יותר
    corner_points = []
    if corners is not None:
        for corner in corners:
            x, y = corner.ravel()
            corner_points.append((int(x), int(y)))

    # מאחדים את כל הנקודות שמצאנו ומסירים כפילויות
    all_raw_points = list(set(edge_points + corner_points)) # Remove duplicates

    # מוסיפים ידנית נקודות חשובות בקצוות ובמרכזי התמונה, כדי לכסות את כל האזור
    all_raw_points.extend([(0,0), (width-1,0), (0,height-1), (width-1,height-1)])
    all_raw_points.extend([(width//2, 0), (0, height//2), (width-1, height//2), (width//2, height-1)])
    all_raw_points.extend([(width//4, 0), (width*3//4, 0), (0, height//4), (0, height*3//4)])
    all_raw_points.extend([(width-1, height//4), (width-1, height*3//4), (width//4, height-1), (width*3//4, height-1)])

    # שלב 2: סינון נקודות קרובות מדי כדי למנוע משולשים קטנים מדי

    # מרחק מינימלי בין נקודות
    # הקטנה משמעותית שלו כדי להבטיח כיסוי טוב יותר, במיוחד בקצוות
    min_point_distance = 8

    filtered_points = []
    # ממיינים את הנקודות
    all_raw_points.sort(key=lambda p: (p[1], p[0]))

    for p in all_raw_points:
        is_far_enough = True
        for existing_p in filtered_points:
            if distance(p, existing_p) < min_point_distance:
                is_far_enough = False
                break
        if is_far_enough:
            filtered_points.append(p)

    # ממירים את הנקודות למערך מתאים לטריאנגולציה
    points_for_subdiv = np.array(filtered_points, dtype=np.float32).reshape(-1, 2)
    print(f"Filtered down to {len(points_for_subdiv)} points for triangulation.")


    if len(points_for_subdiv) < 3:
        print("Not enough unique points detected to form triangles. Adjust parameters.")
    else:
        # שלב 3: טריאנגולציה - פירוק התמונה למשולשים לפי הנקודות

        # מגדיר את גבולות השטח שבו תתבצע הטריאנגולציה (כל התמונה)
        rect = (0, 0, img.shape[1], img.shape[0])
        # יוצר אובייקט טריאנגולציה דלונית של OpenCV (מנוע הטריאנגולציה)
        subdiv = cv.Subdiv2D(rect)

        # מוסיפים את כל הנקודות לטריאנגולציה
        for p in points_for_subdiv:
            # בדיקה: רק נקודות שבתוך גבולות התמונה נכנסות (כדי למנוע שגיאות)
            if 0 <= p[0] < width and 0 <= p[1] < height:
                 # כאן מתבצע לא רק "הוספה", אלא עדכון רשת המשולשים כך שתישמר טריאנגולציה דלונית
                subdiv.insert(tuple(p))

        # לאחר שכל הנקודות הוכנסו, שולפים את רשימת המשולשים שנוצרו
        triangle_list = subdiv.getTriangleList()

        img_triangles = img.copy() #  עותק של התמונה המקורית – עליו נצייר את קווי המשולשים
        img_filled_triangles = img.copy() #  עותק נוסף – עליו נמלא את המשולשים בצבע הממוצע שלהם
        all_triangles_data = [] # כאן נשמור את נתוני המשולשים
        min_area_threshold = 10 # שטח מינימלי למשולש (מדלגים על קטנים מדי)
        margin_x = 10  # מרווח לבדיקה אם משולש בגבול התמונה
        margin_y = 10

        for t in triangle_list:
            # קואורדינטות של שלושת קודקודי המשולש
            pt1_2d = (int(t[0]), int(t[1]))
            pt2_2d = (int(t[2]), int(t[3]))
            pt3_2d = (int(t[4]), int(t[5]))

            # בודקים שכל הקודקודים בגבולות התמונה (עם מרווח)
            if ((-margin_x <= pt1_2d[0] < img.shape[1] + margin_x) and (-margin_y <= pt1_2d[1] < img.shape[0] + margin_y) and
                (-margin_x <= pt2_2d[0] < img.shape[1] + margin_x) and (-margin_y <= pt2_2d[1] < img.shape[0] + margin_y) and
                (-margin_x <= pt3_2d[0] < img.shape[1] + margin_x) and (-margin_y <= pt3_2d[1] < img.shape[0] + margin_y)):

                # מחשבים את שטח המשולש
                area = 0.5 * abs(pt1_2d[0]*(pt2_2d[1] - pt3_2d[1]) + pt2_2d[0]*(pt3_2d[1] - pt1_2d[1]) + pt3_2d[0]*(pt1_2d[1] - pt2_2d[1]))

                # אם השטח גדול מסף המינימום, המשך בעיבוד המשולש
                if area > min_area_threshold:
                    # ממירים את שלושת קודקודי המשולש למערך numpy (נדרש לפונקציות של OpenCV)
                    triangle_pts_2d_np = np.array([pt1_2d, pt2_2d, pt3_2d], np.int32)

                    # יוצרים מסיכה של המשולש כדי לחשב צבע ממוצע
                    mask = np.zeros(img.shape[:2], dtype=np.uint8)
                    cv.fillPoly(mask, [triangle_pts_2d_np], (255)) # מילוי המשולש בלבן על המסיכה

                    # בודקים שיש פיקסלים במסיכה
                    if np.any(mask == 255):

                        # לוקח את ערכי הצבע של כל הפיקסלים שבתוך המשולש בלבד,
                        # ע"י שימוש במסיכה: mask==255 מסמן בדיוק את הפיקסלים שנמצאים במשולש,
                        # וכך אפשר לחשב ממוצע צבע רק עבורם בקלות וביעילות.
                        b_values = img[:, :, 0][mask == 255]
                        g_values = img[:, :, 1][mask == 255]
                        r_values = img[:, :, 2][mask == 255]

                        # מחשבים ממוצע לכל ערוץ צבע
                        avg_b = int(np.mean(b_values))
                        avg_g = int(np.mean(g_values))
                        avg_r = int(np.mean(r_values))
                        average_color_bgr = (avg_b, avg_g, avg_r)
                        average_color_rgb = (avg_r, avg_g, avg_b) # המרה ל-RGB עבור הפלט וה-JSON

                        # שומרים את נתוני המשולש (נקודות וצבע)
                        pt1_3d = [pt1_2d[0], pt1_2d[1], 0]
                        pt2_3d = [pt2_2d[0], pt2_2d[1], 0]
                        pt3_3d = [pt3_2d[0], pt3_2d[1], 0]

                        # אחסון נתוני המשולש (נקודות וצבע)
                        all_triangles_data.append({
                            "points": [pt1_3d, pt2_3d, pt3_3d],
                            "color_rgb": average_color_rgb
                        })

                        # מצייר שלושה קווים ירוקים בין קודקודי המשולש על img_triangles,
                        # כך שכל משולש ייראה כרשת קווים (wireframe) ברורה.
                        cv.line(img_triangles, pt1_2d, pt2_2d, (0, 255, 0), 1)
                        cv.line(img_triangles, pt2_2d, pt3_2d, (0, 255, 0), 1)
                        cv.line(img_triangles, pt3_2d, pt1_2d, (0, 255, 0), 1)

                        # ממלאים את המשולש בצבע הממוצע בתמונה נוספת
                        cv.fillPoly(img_filled_triangles, [triangle_pts_2d_np], average_color_bgr)
                        cv.polylines(img_filled_triangles, [triangle_pts_2d_np], True, (0,0,0), 1) # הוספת גבול שחור אופציונלי

        print(f"Total triangles generated: {len(all_triangles_data)}")

        # שומרים את נתוני המשולשים לקובץ JSON
        output_json_filename = 'triangles_data.json'
        with open(output_json_filename, 'w', encoding='utf-8') as f:
            json.dump(all_triangles_data, f, ensure_ascii=False, indent=4)
        print(f"\nTriangle data saved to JSON file: {output_json_filename}")

        # שומרים גם לקובץ טקסט
        output_txt_filename = 'triangles_data.txt'
        with open(output_txt_filename, 'w', encoding='utf-8') as f_txt:
            for tri_data in all_triangles_data:
                coords = tri_data["points"]
                color = tri_data["color_rgb"]
                # פורמט שטוח: x1,y1,z1,x2,y2,z2,x3,y3,z3,R,G,B
                flat_values = [str(val) for point in coords for val in point] + [str(c) for c in color]
                f_txt.write(','.join(flat_values) + '\n')

        print(f"Triangle data also saved to text file: {output_txt_filename}")

        # מציגים את התמונות: מקורית, קצוות, משולשים בקווים, משולשים ממולאים
        cv.imshow('Original Image', img)
        cv.imshow('Detected Edges (Canny)', edges)
        cv.imshow('Triangulated Image (Lines)', img_triangles)
        cv.imshow('Triangulated Image (Filled with Avg Color)', img_filled_triangles)
        cv.waitKey(0)
        cv.destroyAllWindows()