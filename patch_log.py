import re

with open("sam3-service/app.py", "r") as f:
    content = f.read()

content = content.replace("print(\"Running Hybrid OpenCV + Gemini Multimodal Vision Extraction...\")", 
"""print("Running Hybrid OpenCV + Gemini Multimodal Vision Extraction...")
    print(f"OpenCV found {len(opencv_lines) if opencv_lines else 0} lines.")""")

content = content.replace("extraction = json.loads(response.text)",
"""extraction = json.loads(response.text)
        print("Gemini Extraction:", extraction)""")

with open("sam3-service/app.py", "w") as f:
    f.write(content)
