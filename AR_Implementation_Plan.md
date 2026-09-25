# Plan de Arquitectura y Optimización AR (Realidad Mixta) Híbrida

Este plan detalla la arquitectura óptima, ultra-ligera y nativa para dispositivos Android (APK) en Realidad Mixta, implementando un sistema híbrido de anclaje para máxima precisión en el despliegue de Glasfaser.

## Arquitectura Propuesta (La alternativa ultra-ligera)

La meta es que la app consuma mínima batería y maximice los FPS, prescindiendo del renderizado de mapas pesados.

### 1. El mundo virtual desaparece (Passthrough Only)
- No cargaremos modelos de terreno, mapa de altura, ni fotos satelitales (0% dependencia de Cesium en el APK final).
- El fondo de la aplicación final en Unreal Engine será totalmente transparente (Alpha 0). Las cámaras proveerán el mundo real.

### 2. Conversión Matemática Local (Reemplazo de Cesium)
En lugar de cargar un motor geográfico, utilizaremos pura matemática en C++:
- El backend (`QuarkBauRemoteRepository`) envía coordenadas Lat/Lon.
- Implementaremos un **Traductor de Plano Tangente Local (Local Tangent Plane)**. Elegimos un punto del clúster como `(0,0,0)` y calculamos distancias métricas (X,Y).

### 3. Sistema de Anclaje Híbrido (Auto/Manual)

> **El "Santo Grial" del AR en exteriores: VPS (Visual Positioning System).**  
> Para lograr el anclaje automático sin interacción del operario, delegaremos el reconocimiento del entorno en el teléfono móvil.

**Modo A: Calibración Automática (Teléfono Disponible - Recomendado para XReal)**
- Utilizaremos la **API Geoespacial de ARCore (Google VPS)** en el teléfono móvil.
- **¿Cómo funciona?** El teléfono usa su cámara por unos segundos, toma la imagen de los edificios reales y la cruza con la base de datos de *Google Street View* en la nube.
- El teléfono deduce su posición global real y su rotación con precisión de centímetros.
- Establece automáticamente el Origen `(0,0,0)` y proyecta los tubos holográficos perfectamente alineados. (Si usas XReal conectadas al móvil, esto funciona de forma nativa).

**Modo B: Calibración Manual (Fallback / Quest 3 Standalone / Zona sin Street View)**
- Si el operario no usa el teléfono, o está en una calle nueva/rural que no existe en Google Street View, la app pasa al modo manual.
- El sistema OpenXR de las gafas se activa. El operario alinea visualmente un "holograma de calibración" con un punto físico conocido (ej. una tapa de registro) durante 5 segundos.
- Una vez alineado, el *Spatial Anchor* (Anclaje Espacial) local se fija y el SLAM de las gafas toma el control para que los tubos no se muevan al caminar.

### 4. Flujo de Trabajo de Desarrollo (Cesium como Debugger Visual)
Aunque Cesium no formará parte de la aplicación compilada (APK), se conservará en el proyecto de Unreal como herramienta de desarrollo.
- **Ventaja:** Permite visualizar el terreno global y fotos satelitales en el Editor de Unreal mientras se programa, asegurando que las matemáticas y los segmentos generados sean correctos.
- **Implementación Segura:** Para evitar que contamine el APK, se utilizará una estrategia de separación por niveles. El terreno de Cesium vivirá exclusivamente en un mapa de desarrollo (ej. `Mapa_Desarrollo_Cesium`), mientras que el mapa que se empaqueta para las gafas será un mapa puramente AR (ej. `Mapa_Produccion_AR`). Cualquier código C++ relacionado con Cesium se aislará de la compilación final usando macros como `#if WITH_EDITOR`.

## Fases de Implementación en Unreal (HorizonVision)

1.  **Configuración del Motor y Niveles:**
    - Relegar Cesium a mapas exclusivos del Editor y aislar dependencias de código (`WITH_EDITOR`).
    - Activar OpenXR, el SDK de las gafas (XReal/Meta) y los modos de Passthrough.

2.  **Integración del Teléfono (VPS):**
    - Integrar el plugin nativo de *Google ARCore Geospatial* (disponible para Unreal Engine/Android) para manejar la localización visual de alta precisión al arrancar la app.

3.  **Módulo Matemático (`GeoSpatialSubsystem.cpp`):**
    - Escribir conversores (Lat/Lon a Local CM).

4.  **Renderizado Ultra-Ligero:**
    - Usar **Spline Meshes** sin cálculo de sombras de luz para dibujar la fibra.
