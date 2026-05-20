# CardsGame

<p align="center">

  <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white"/>
  <img src="https://img.shields.io/badge/Versión-2.0-red?style=for-the-badge"/>
  <img src="https://img.shields.io/badge/Estado-En%20Desarrollo-yellow?style=for-the-badge"/>
  <img src="https://img.shields.io/badge/Proyecto-Universitario-blue?style=for-the-badge"/>
  <img src="https://img.shields.io/badge/Licencia-MIT-green?style=for-the-badge"/>
</p>

## Descargas

Los binarios ejecutables estan publicados en `dist/`.

| Version | Rama origen | Interfaz | Descarga |
| --- | --- | --- | --- |
| V1 | `Acxel` | Consola | [CardsGame-V1-console.jar](https://github.com/h4ckxel/CardsGame/raw/main/dist/CardsGame-V1-console.jar) |
| V2 | `UNOGame_v4` | Consola | [CardsGame-V2-console.jar](https://github.com/h4ckxel/CardsGame/raw/main/dist/CardsGame-V2-console.jar) |
| V3 | `main` | Swing | [CardsGame-V3-gui.jar](https://github.com/h4ckxel/CardsGame/raw/main/dist/CardsGame-V3-gui.jar) |

## Ejecucion

Requisito: Java 17 o superior.

V1 y V2 son aplicaciones de consola. Deben ejecutarse desde una terminal para poder ingresar datos por teclado:

```bash
java -jar CardsGame-V1-console.jar
java -jar CardsGame-V2-console.jar
```

V3 abre la interfaz grafica por defecto:

```bash
java -jar CardsGame-V3-gui.jar
```

V3 tambien puede ejecutarse en modo consola:

```bash
java -jar CardsGame-V3-gui.jar --consola
```

## Descripcion tecnica

El proyecto modela el flujo basico de una partida de UNO mediante clases de dominio para cartas, baraja, mano, jugadores y motor de juego. Las versiones V2 y V3 separan mejor las responsabilidades del registro de jugadores, control de turnos, efectos de cartas y salida por consola. La version V3 agrega una capa Swing y recursos graficos para representar las cartas.

## Estructura principal

```text
UnoGame/
  src/main/java/unogame/
    UNOGame.java
    clases/
    ui/
  src/main/resources/cartas/
dist/
  CardsGame-V1-console.jar
  CardsGame-V2-console.jar
  CardsGame-V3-gui.jar
```

## Autores

- Mariana: [@Mariana08022003](https://github.com/Mariana08022003)
- Joshua: [@JoshuaIbarra03](https://github.com/JoshuaIbarra03)
- Sebastian: [@SebastiaN988Tv](https://github.com/SebastiaN988Tv)
- Omar: [@wumaro2000](https://github.com/wumaro2000)
- Acxel: [@h4ckxel](https://github.com/h4ckxel)
