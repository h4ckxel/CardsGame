# CardsGame — UNO en Java

<p align="center">
  <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white"/>
  <img src="https://img.shields.io/badge/Estado-En%20Desarrollo-yellow?style=for-the-badge"/>
  <img src="https://img.shields.io/badge/Proyecto-Universitario-blue?style=for-the-badge"/>
  <img src="https://img.shields.io/badge/Licencia-MIT-green?style=for-the-badge"/>
</p>



## Objetivos del Proyecto

- Aplicar los principios de la **Programación Orientada a Objetos (POO)** en un proyecto real.
- Diseñar e implementar la **lógica de un juego de cartas** desde cero.
- Practicar el uso de estructuras de datos como pilas, listas y colas.
- Familiarizarse con el manejo de **flujo de turnos**, validaciones de reglas y estados del juego.
- Sentar las bases para futuras implementaciones con interfaz gráfica y multijugador.

---

## Estructura del Proyecto

```
CardsGame/
└── UnoGame/
    ├── src/
    │   ├── Card.java          # Modelo de carta (color, valor, tipo)
    │   ├── Deck.java          # Mazo: generación y reparto de cartas
    │   ├── Player.java        # Lógica del jugador y su mano
    │   ├── Game.java          # Motor principal del juego y turnos
    │   └── Main.java          # Punto de entrada de la aplicación
    └── ...
```

> La estructura puede variar conforme avance el desarrollo.

---

## ¿Cómo ejecutar el proyecto?

### Requisitos previos

- **Java JDK 11** o superior instalado.
- Un IDE como **IntelliJ IDEA**, **Eclipse** o **VS Code** con soporte para Java.
- O simplemente la terminal con `javac` y `java`.

### Pasos

```bash
# 1. Clonar el repositorio
git clone https://github.com/h4ckxel/CardsGame.git

# 2. Entrar al directorio del juego
cd CardsGame/UnoGame

# 3. Compilar los archivos Java
javac -d bin src/*.java

# 4. Ejecutar el juego
java -cp bin Main
```

---

## Reglas implementadas

La versión actual del juego incluye las reglas básicas del UNO:

- ✅ Reparto inicial de cartas a los jugadores.
- ✅ Turno por turno con validación de jugadas.
- ✅ Cartas de acción: **+2**, **Saltar turno**, **Invertir dirección**.
- ✅ Carta comodín: **cambio de color**.
- ✅ Condición de victoria al quedarse sin cartas.
- ✅ Penalización por no gritar "UNO".

---

## Mejoras planeadas

Este proyecto continuará desarrollándose. Las siguientes funcionalidades están en la hoja de ruta:

| # | Mejora | Estado |
|---|--------|--------|
| 1 | Interfaz gráfica (GUI) con Java Swing o JavaFX | Pendiente |
| 2 | Modo multijugador en red (Sockets TCP/IP) | Pendiente |
| 3 | Inteligencia Artificial para jugadores bot | Pendiente |
| 4 | Sistema de puntuación y estadísticas | Pendiente |
| 5 | Soporte para más variantes del UNO (UNO Flip, etc.) | Pendiente |
| 6 | Persistencia de partidas (guardado/carga) | Pendiente |
| 7 | Sonidos y animaciones de cartas | Pendiente |
| 8 | Pruebas unitarias con JUnit | Pendiente |

---

## Conceptos de POO aplicados

| Concepto | Aplicación en el proyecto |
|----------|---------------------------|
| **Clases y Objetos** | `Card`, `Deck`, `Player`, `Game` |
| **Herencia** | Tipos de carta que extienden la clase base `Card` |
| **Encapsulamiento** | Atributos privados con getters/setters |
| **Polimorfismo** | Comportamiento distinto por tipo de carta al jugarse |
| **Abstracción** | Separación de lógica del juego y representación de datos |

---

## Autores


**Mariana**
- GitHub: [@Mariana08022003](https://github.com/Mariana08022003)

**Joshua**
- GitHub: [@JoshuaIbarra03](https://github.com/JoshuaIbarra03)

**Sebastian**
- GitHub: [@SebastiaN988Tv](https://github.com/SebastiaN988Tv)

**Omar**
- GitHub: [@wumaro2000](https://github.com/wumaro2000)

**Acxel**
- GitHub: [@h4ckxel](https://github.com/h4ckxel)
---

## Licencia

Este proyecto está bajo la licencia **MIT**. Consulta el archivo [LICENSE](LICENSE) para más detalles.

---

> 💡 *Este proyecto es de carácter académico y está en constante evolución. Las contribuciones, sugerencias y pull requests son bienvenidos.*