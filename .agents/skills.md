# Agent Skills

Este archivo detalla las habilidades especiales (skills) disponibles para los agentes que trabajan en este proyecto.

## Skills Disponibles

Todos los skills están ubicados en `.agents/skills/`:

### OpenSpec Skills
- `openspec-propose` - Genera propuestas de cambio
- `openspec-apply-change` - Aplica cambios aprobados
- `openspec-update-change` - Actualiza propuestas existentes
- `openspec-archive-change` - Archiva cambios completados
- `openspec-sync-specs` - Sincroniza specs
- `openspec-explore` - Explorador de specs

Ver documentación completa en cada carpeta `.agents/skills/openspec-*/`

### Caveman Skill
- **Ubicación**: `.agents/skills/caveman/`
- **Modo por defecto**: caveman (respuestas breves y técnicas)
- **Comportamiento**: Máxima compresión, sin relleno, directo al punto
- **Modos disponibles**: caveman, ultracave, megacave

Ver documentación completa en `.agents/skills/caveman/skill.md`

### Otras Herramientas de Ingeniería
- `investigate-first` - Diagnóstico de fallos ambiguos
- `lean-build` - Construcción con alcance estricto
- `safe-refactor` - Reestructuración preservando comportamiento
- `surgical-patch` - Corrección en capa más estrecha
- `verify-and-stop` - Validación de aceptación
