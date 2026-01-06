# Sistema de Animaciones - Junta Core Mod

## 📁 Estructura de Carpetas

Las animaciones deben guardarse en la carpeta de recursos con la siguiente estructura:

```
src/main/resources/assets/junta/textures/gui/animations/
├── muerte/
│   ├── 0000.png
│   ├── 0001.png
│   ├── 0002.png
│   └── ...
├── explosion/
│   ├── 0000.png
│   ├── 0001.png
│   └── ...
└── [nombre_animacion]/
    ├── 0000.png
    ├── 0001.png
    └── ...
```

## 🖼️ Especificaciones de Imágenes

### Resolución:
- **Todas las imágenes**: 512x512 píxeles
- El sistema automáticamente reescalará las imágenes según el tamaño especificado
- **Normal**: Renderiza a 512x512 píxeles (tamaño completo)
- **Small**: Renderiza a 256x256 píxeles (reescalado por el código)

### Formato:
- **Formato**: PNG con transparencia
- **Nomenclatura**: `XXXX.png` (donde XXXX es un número de 4 dígitos)
  - Ejemplos: `0000.png`, `0001.png`, `0002.png`, `0178.png`
- **Numeración**: Comienza desde 0000 y continúa secuencialmente

## 🎮 Uso del Comando

### Sintaxis Básica:
```
/jntore animation <nombre> <posición> <tamaño> [fps]
```

### Parámetros:

#### 1. **nombre**: Nombre de la carpeta de animación
- Ejemplo: `muerte`, `explosion`, `victoria`

#### 2. **posición**: Ubicación en pantalla
- `centro` - Centro de la pantalla
- `scoreboard` - Centro pero un poco más abajo (cerca del marcador)
- `up_derecha` - Esquina superior derecha
- `up_izquierda` - Esquina superior izquierda
- `down_derecha` - Esquina inferior derecha
- `down_izquierda` - Esquina inferior izquierda

#### 3. **tamaño**: Tamaño de la animación
- `normal` - 512x512 píxeles
- `small` - 256x256 píxeles

#### 4. **fps** (opcional): Fotogramas por segundo
- **Por defecto**: 24 FPS
- **Rango**: 1-120 FPS
- Si no se especifica, se usa 24 FPS

## 📝 Ejemplos de Uso

### Ejemplo 1: Animación básica (24 FPS por defecto)
```
/jntore animation muerte centro normal
```

### Ejemplo 2: Animación con FPS personalizado
```
/jntore animation muerte centro normal 60
```

### Ejemplo 3: Animación pequeña en esquina
```
/jntore animation explosion up_derecha small 30
```

### Ejemplo 4: Animación cerca del scoreboard
```
/jntore animation victoria scoreboard normal 24
```

## 🛑 Detener Animación

Para detener una animación en reproducción:
```
/jntore animation stop
```

## ⚙️ Características Técnicas

- **Carga Automática**: El sistema detecta automáticamente cuántos frames hay en cada carpeta
- **Reproducción Única**: La animación se reproduce una vez y luego desaparece
- **Sincronización**: Las animaciones se sincronizan con todos los jugadores en el servidor
- **Permisos**: Requiere nivel de permisos 2 (operador)

## 🎨 Tips para Crear Animaciones

1. **Resolución Estándar**: Todas las imágenes deben ser 512x512 píxeles
2. **Reescalado Automático**: El código reescalará automáticamente las imágenes según el tamaño elegido (small = 256px, normal = 512px)
3. **Optimización**: Mantén tus imágenes optimizadas para no afectar el rendimiento
4. **Transparencia**: Usa transparencia (canal alpha) para efectos visuales limpios
5. **Nomenclatura Simple**: Usa solo números: 0000.png, 0001.png, 0002.png...
6. **FPS Apropiado**: 
   - 24 FPS: Animaciones cinematográficas
   - 30 FPS: Animaciones fluidas estándar
   - 60 FPS: Animaciones muy suaves (más pesado)

## 📊 Posicionamiento Visual

```
┌─────────────────────────────────┐
│  up_izquierda    up_derecha    │
│                                 │
│          centro                 │
│                                 │
│        scoreboard               │
│                                 │
│  down_izquierda  down_derecha  │
└─────────────────────────────────┘
```

## 🔧 Troubleshooting

### La animación no aparece:
- Verifica que los frames estén en la carpeta correcta
- Asegúrate de que los archivos sigan el formato `XXXX.png` (ejemplo: 0000.png, 0001.png)
- Comprueba que las imágenes sean PNG válidos de 512x512 píxeles

### La animación se ve borrosa:
- Verifica que todas las imágenes sean exactamente 512x512 píxeles
- Asegúrate de que las imágenes originales tengan buena calidad
- El reescalado para "small" puede hacer que se vea ligeramente menos nítido

### La animación va muy rápida/lenta:
- Ajusta el parámetro FPS en el comando
- Considera agregar o eliminar frames intermedios
