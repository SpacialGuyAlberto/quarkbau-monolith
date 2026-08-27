# Plan de Implementación: Pipeline de Reconocimiento Smart Segment (OCR & CV)

Para lograr que un modelo de Computer Vision tome una "imagen plana" (como un PDF escaneado o un JPEG de un Planauskunft) y termine arrojando coordenadas exactas en Google Maps, necesitas un **pipeline (cadena) de varios algoritmos**, ya que un solo algoritmo no hace todo el trabajo.

El proceso se divide en dos grandes fases: **Reconocimiento de la línea (Vectorización)** y **Georreferenciación (Alineación en el mapa)**. Aquí se explican los algoritmos estándar de la industria (PropTech/GIS) para lograrlo:

## Fase 1: Reconocimiento y Extracción de las Líneas
El objetivo aquí es convertir píxeles de colores (una mancha roja en la imagen) en vectores matemáticos (una línea).

1. **Segmentación Semántica (U-Net o DeepLabV3+):**
    *   **¿Qué es?** Son redes neuronales profundas (Deep Learning) especializadas en clasificar *cada píxel* de una imagen. 
    *   **¿Cómo se usa?** Entrenas a la **U-Net** para que mire el plano y separe el ruido: "Píxel blanco = fondo, Píxel gris = calle, Píxel rojo = Cable principal de Fibra, Píxel azul = Acometida (Hausanschluss)".

2. **Esqueletización (Algoritmo de Zhang-Suen o Thinning):**
    *   **¿Qué es?** Un algoritmo clásico de visión computacional.
    *   **¿Cómo se usa?** El modelo U-Net del paso anterior te va a devolver una "línea gorda" (de varios píxeles de ancho). La esqueletización va "pelando" los bordes de esa mancha hasta reducirla a una línea perfecta de **1 solo píxel de grosor**.

3. **Algoritmo de Douglas-Peucker (y Transformada de Hough):**
    *   **¿Para qué sirve?** Transforma esos píxeles sueltos en nodos reales (Punto A al Punto B). La transformada de Hough detecta rectas perfectas, y el algoritmo de Douglas-Peucker simplifica las curvas para que no tengas un millón de nodos, sino una `LineString` limpia como la que guardarías en la base de datos.

---

## Fase 2: Posicionamiento Exacto en el Mapa (Georreferenciación)
Ahora tienes una línea vectorial flotando en el vacío (ej. que va del píxel `x:100, y:200` al `x:500, y:400`). Hay que anclarla al mundo real (Latitud/Longitud).

4. **Extracción de Puntos Clave (SIFT, SURF u ORB):**
    *   **¿Qué son?** Algoritmos de "Feature Matching" (Emparejamiento de características). 
    *   **¿Cómo se usa?** El algoritmo analiza las calles o esquinas de los edificios dibujados en tu Planauskunft y extrae "puntos clave" (esquinas muy distintivas). Al mismo tiempo, hace lo mismo sobre una captura de Google Maps (o los datos de OpenStreetMap) del área de tu proyecto.
    *   **La Magia:** El algoritmo empareja automáticamente las esquinas del dibujo con las esquinas reales del satélite.

5. **Transformación Afín (Affine Transformation Matrix):**
    *   **¿Qué es?** Es una operación matemática determinista de álgebra lineal.
    *   **¿Cómo se usa?** Una vez que el algoritmo SIFT/ORB encontró al menos 3 o 4 puntos en común (Ground Control Points) entre tu plano y el mapa real, aplicamos una Matriz de Transformación Afín. Esta fórmula matemática **escala, rota y traslada** automáticamente todo el dibujo (incluyendo las líneas de fibra que extrajimos en la Fase 1) para que encaje perfectamente sobre las coordenadas geográficas de Google Maps.

---

## Resumen del Flujo Ideal (Tech Stack sugerido para esto)
1. **Input:** Usuario sube PDF rasterizado.
2. **Backend (Python / PyTorch):** Pasa la imagen por **U-Net** (o SAM 2 en modo interactivo) para encontrar dónde están los cables.
3. **OpenCV:** Aplica esqueletización y extrae vértices (píxeles `x, y`).
4. **Backend (Geopandas / Rasterio / OpenCV):** Usa **ORB** para comparar las calles del plano con un mapa de OpenStreetMap, calculando la matriz de rotación y escala.
5. **Salida:** Python aplica la matriz a los píxeles, los convierte a Latitud/Longitud, genera un archivo `.geojson` o un JSON vía API REST y se lo envía a tu monolito en Java, el cual lo lee y lo guarda en la base de datos (como se implementó en `SmartSegmentRecognitionService`).
