# Mutual Action Plan (Pilotprojekt Roadmap)
**Proyecto:** Digitalización y Gemelo Digital FTTx
**Cliente:** German Infrastructure GmbH
**Socio Tecnológico:** QuarkBau
**Duración del Piloto:** 9 Semanas

---

## 🎯 Objetivo del Proyecto Piloto
Demostrar de forma empírica y medible que el ecosistema QuarkBau (ERP + Edge AI + HorizonVision AR/VR) es capaz de:
1. **Reducir el tiempo administrativo** de los *Bauleiter* y *Projektleiter* en un 40%.
2. **Eliminar los Bauschäden** (daños a infraestructuras de terceros) mediante la pre-visualización en Realidad Aumentada.
3. **Automatizar la generación de Aufmaße** (certificaciones de avance) con un 100% de precisión geográfica (As-Built en tiempo real).

---

## 📅 Cronograma de Implementación (4 Fases)

### Fase 1: Setup Técnico y Gemelo Digital (Semanas 1 - 2)
*El objetivo de esta fase es conectar los datos de planificación en papel o CAD de German Infrastructure al motor espacial de QuarkBau.*
* **Hito 1.1:** Selección del *Baulos* (zona de obra) específico para el piloto (Ej: 1 POP, 5 DPs, ~5km de zanja).
* **Hito 1.2:** Ingesta de datos GIS / Planos originales en la base de datos PostGIS de QuarkBau.
* **Hito 1.3:** Creación del Gemelo Digital 3D (HorizonVision) para visualización en oficina.
* **Responsable:** Arquitectura Principal (QuarkBau).
* **Requisito del Cliente:** Proveer los planos en formato digital (CAD/GIS/PDF) de la zona seleccionada.

### Fase 2: Hardware Onboarding & Training (Semana 3)
*Despliegue del equipo en terreno y capacitación de la cuadrilla seleccionada.*
* **Hito 2.1:** Entrega de hardware al *Bauleiter* y *Teamleiter* (Tablets con Edge AI y Gafas AR).
* **Hito 2.2:** Formación in-situ de 4 horas con la cuadrilla elegida (Onboarding de la Mobile App y reportes por voz).
* **Hito 2.3:** Prueba en vacío (Mock-test) de una *Smart Videoconference* entre la zanja y la oficina.
* **Responsable:** Consultoría QuarkBau + German Infrastructure (Bauleiter asignado).

### Fase 3: Ejecución en Terreno (*Field Rollout*) (Semanas 4 - 8)
*Operación real en la calle. El sistema funciona en vivo como única fuente de la verdad.*
* **Semana 4:** Inicio de excavación. Los obreros utilizan AR para validar rutas y evitar colisiones con gas/agua.
* **Semana 5-6:** Reporte diario de hitos (Tiefbau, Rohreinzug, Asfalto) documentado con fotos geolocalizadas y comandos de voz (Edge AI).
* **Semana 7-8:** El *Bauleiter* audita el progreso de forma remota usando el Dashboard Web y aprueba desviaciones (*Redlining*) en 3D.
* **Responsable:** Cuadrilla de German Infrastructure (Ejecución) + Soporte Técnico Directo (QuarkBau).

### Fase 4: Auditoría de ROI y Go/No-Go (Semana 9)
*Evaluación financiera y técnica del piloto.*
* **Hito 4.1:** Consolidación automática de los *Aufmaße* generados por el sistema vs. los métodos tradicionales.
* **Hito 4.2:** Reunión de evaluación con el *Projektleiter* y Dirección de German Infrastructure.
* **Hito 4.3:** Presentación del reporte de incidencias prevenidas y horas administrativas ahorradas.
* **Hito 4.4 (Cierre):** Firma del acuerdo marco para el despliegue a escala en todos los proyectos de la compañía (*Rollout General*).

---

## 📊 KPIs y Criterios de Éxito (Success Metrics)
Para que el Piloto sea considerado un éxito y pasemos a la fase de contrato general, se deben cumplir las siguientes métricas:
1. **Adopción en terreno:** 100% de los reportes diarios enviados vía QuarkBau App (sin usar WhatsApp ni papel).
2. **Precisión As-Built:** Las geometrías capturadas por el *Bauleiter* vía GPS/AR coinciden con el terreno físico sin necesidad de topografía posterior.
3. **Reducción de Latencia Documental:** El *Aufmaß* mensual se puede exportar en menos de 1 hora el último día de mes.

---

## 💰 Inversión del Piloto (Proof of Concept Setup Fee)
El coste de ejecución de este Piloto de 9 semanas es un **pago único fijo de 5.500€** (ajustable según el tamaño exacto del Baulos). Este importe cubre exclusivamente los costes operativos de implementación para que German Infrastructure pueda evaluar la tecnología sin riesgos ocultos.

**El Setup Fee incluye:**
1. Ingesta manual y estructuración de vuestros datos GIS/CAD al Gemelo Digital 3D de QuarkBau.
2. Préstamo y seguro del hardware tecnológico durante el piloto (Gafas AR y Tablets con Edge AI).
3. Desplazamiento personal a vuestra obra para la formación in-situ de la cuadrilla (*Onboarding*).
4. Licencias de software en la nube y soporte técnico 24/7 ilimitados durante los 2 meses.

> **Cláusula de Garantía de Éxito:** Si tras auditar los resultados en la Fase 4, los KPIs se cumplen y German Infrastructure decide firmar un contrato anual de despliegue a escala, **estos 5.500€ iniciales serán descontados íntegramente de dicho contrato final**. En otras palabras: si el sistema demuestra su valor, probar el piloto os habrá costado 0€.

---
*Firma de Acuerdo de Piloto:*

_______________________
**German Infrastructure GmbH** 
(Projektleiter)

_______________________
**QuarkBau**
(Arquitecto Principal / Fundador)
