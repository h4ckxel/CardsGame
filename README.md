# CardsGame — UNO en Java

<p align="center">
  <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white"/>
  <img src="https://img.shields.io/badge/Versión-2.0-red?style=for-the-badge"/>
  <img src="https://img.shields.io/badge/Estado-En%20Desarrollo-yellow?style=for-the-badge"/>
  <img src="https://img.shields.io/badge/Proyecto-Universitario-blue?style=for-the-badge"/>
  <img src="https://img.shields.io/badge/Licencia-MIT-green?style=for-the-badge"/>
</p>

---

## ¿Qué hay de nuevo en la v2.0?

Esta versión representa una **refactorización completa** del proyecto. Se rediseñó la arquitectura para respetar los principios de responsabilidad única y separación de responsabilidades, se completaron las reglas del UNO clásico, se introdujo soporte para múltiples jugadores y se añadió una experiencia visual en consola con colores y animaciones.

### Resumen de cambios

| # | Cambio | Descripción |
|---|--------|-------------|
| 1 | **Nueva clase `GestorTurnos`** | Centraliza toda la lógica del sentido del juego usando la fórmula con módulo |
| 2 | **Nueva clase `Player`** | Gestiona el registro de jugadores (1 humano + 1 a 9 IA, máximo 10) |
| 3 | **Nueva clase `Consola`** | Centraliza colores ANSI, animaciones de loading y estilos visuales |
| 4 | **`Juego` refactorizado** | Ahora solo coordina el flujo y las reglas; delegó todo lo demás |
| 5 | **`Jugador` refactorizado** | Separa lógica humana e IA; eliminados índices negativos para robar |
| 6 | **`Mano` mejorada** | Muestra todas las cartas indicando cuáles son jugables con `✓` y `✗` |
| 7 | **`Carta` extendida** | Soporte completo para SALTO, REVERSA, ROBA2, COMODÍN y ROBA4 |
| 8 | **`Baraja` completa** | Genera el mazo UNO clásico oficial (108 cartas) |
| 9 | **REVERSA corregido** | Antes funcionaba igual que SALTO; ahora invierte el sentido real del juego |
| 10 | **Robo obligatorio** | Ya no se puede robar voluntariamente con `-1`; se roba solo si no hay jugada válida |

---

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
    │   ├── main.java               # Punto de entrada
    │   └── clases/
    │       ├── Carta.java          # Modelo de carta: color, valor, tipo especial
    │       ├── Baraja.java         # Mazo completo UNO (108 cartas), reparto y barajeo
    │       ├── Mano.java           # Mano del jugador con sugerencias visuales
    │       ├── Jugador.java        # Jugador humano e IA: decisión de jugada
    │       ├── Player.java         # Registro e inicialización de jugadores (2–10)
    │       ├── GestorTurnos.java   # Sentido del juego, módulo, avance y saltos
    │       ├── Juego.java          # Motor del juego: flujo, reglas y efectos
    │       └── Consola.java        # Colores ANSI, animaciones y estilos de consola
    └── ...
```

> La estructura puede variar conforme avance el desarrollo.

---

## ¿Cómo ejecutar el proyecto?

### Requisitos previos

- **Java JDK 11** o superior instalado.
- Un IDE como **IntelliJ IDEA**, **Eclipse** o **VS Code** con soporte para Java.
- O simplemente la terminal con `javac` y `java`.
- Terminal con soporte para **colores ANSI** (cualquier terminal moderna en Linux/macOS; en Windows usar Windows Terminal o activar ANSI en CMD).

### Pasos

```bash
# 1. Clonar el repositorio
git clone https://github.com/h4ckxel/CardsGame.git

# 2. Entrar al directorio del juego
cd CardsGame/UnoGame

# 3. Compilar los archivos Java
javac -d bin src/clases/*.java src/main.java

# 4. Ejecutar el juego
java -cp bin main
```

---

## Reglas implementadas

| Regla | Estado |
|-------|--------|
| Reparto inicial de 7 cartas por jugador | ✅ |
| Turnos por orden con sentido configurable | ✅ |
| Carta **SALTO** — el siguiente pierde turno | ✅ |
| Carta **REVERSA** — invierte el sentido del juego | ✅ |
| Carta **ROBA 2** — el siguiente roba 2 y pierde turno | ✅ |
| Carta **ROBA 4** — el siguiente roba 4, pierde turno, se elige color | ✅ |
| Carta **COMODÍN** — cambia el color activo | ✅ |
| Regla **UNO** — declarar al quedarse con 1 carta o robar 2 | ✅ |
| Condición de victoria al quedarse sin cartas | ✅ |
| Robo obligatorio solo cuando no hay jugada válida | ✅ |
| Soporte para **2 a 10 jugadores** (1 humano + IA) | ✅ |

---

## Arquitectura v2.0 — Diseño de clases

```
main
 └── Juego
      ├── Player          → registra los jugadores al inicio
      ├── GestorTurnos    → controla índice, dirección y avance de turno
      ├── Baraja          → genera y administra el mazo
      ├── Jugador[]       → cada jugador con su Mano
      │    └── Mano       → lista de Cartas con sugerencias visuales
      └── Consola         → utilidad estática para colores y animaciones
```

### Fórmula del sentido del juego

El avance de turno usa módulo con soporte para dirección negativa (antihorario):

```java
siguiente = ((indiceActual + direccion) % total + total) % total;
```

El `+ total` antes del segundo `%` evita resultados negativos cuando `direccion = -1`, garantizando que el índice siempre sea válido sin importar cuántos jugadores haya.

---

## Conceptos de POO aplicados

| Concepto | Aplicación en el proyecto |
|----------|---------------------------|
| **Clases y Objetos** | `Carta`, `Baraja`, `Mano`, `Jugador`, `Player`, `GestorTurnos`, `Juego`, `Consola` |
| **Encapsulamiento** | Atributos privados con getters/setters en todas las clases |
| **Abstracción** | `Jugador` abstrae la diferencia entre humano e IA con el mismo método `decidirJugada` |
| **Responsabilidad única** | Cada clase tiene una sola razón para cambiar |
| **Separación de responsabilidades** | `Juego` solo coordina; `GestorTurnos` solo maneja turnos; `Consola` solo maneja UI |
| **Delegación** | `Juego` delega registro a `Player`, turnos a `GestorTurnos` y decisiones a `Jugador` |

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
