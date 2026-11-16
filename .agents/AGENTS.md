# AGENTS.md

Este archivo contiene instrucciones y contexto para todos los agentes de IA (Cline, Windsurf, GitHub Copilot, Cursor, etc.) que trabajen en este proyecto.

## Contexto del Proyecto
Este es un integrador para la materia Programación II. Es una aplicación Java para una concesionaria que utiliza una base de datos SQL Server corriendo en Docker/Podman.

## OpenSpec: Desarrollo Dirigido por Especificaciones
Este proyecto utiliza **OpenSpec** para gestionar cambios de manera estructurada. OpenSpec enforce contratos a nivel de campo a través de specs y propuestas de cambio.

### Flujo de Trabajo con OpenSpec
1. **Propuesta**: Usa `/openspec-propose "tu idea"` para generar una propuesta de cambio.
2. **Revisión**: Revisa la propuesta generada en `openspec/changes/`.
3. **Aplicación**: Usa el skill `openspec-apply-change` para aplicar cambios aprobados.
4. **Archivo**: Usa `openspec-archive-change` para archivar cambios completados.

### Configuración de OpenSpec
- **Config**: `openspec/config.yaml` - Define contexto del proyecto y reglas.
- **Specs**: `openspec/specs/` - Especificaciones de campos canónicos.
- **Cambios**: `openspec/changes/` - Propuestas y tareas de cambio.
- **Skills**: `.agents/skills/` - Skills de OpenSpec para agentes.

### Comandos Útiles
- `openspec --version`: Ver versión instalada (actual: 2.0.0)
- `openspec config set telemetry.enabled false`: Deshabilitar telemetría

## Herramientas y Skills: Caveman
Este proyecto utiliza **Caveman** para la observabilidad y optimización de tokens. Todos los agentes deben operar a través de la capa de Caveman.

### Cómo usar Caveman
Si eres un agente CLI o tienes acceso a la terminal:
- Ejecuta tus comandos de agente a través de `caveman`: `caveman <agent_command>`
- Para ver el estado actual: `caveman status`
- Para aprender optimizaciones: `caveman learn`

### Skills Disponibles (para agentes compatibles)
Los siguientes skills están configurados para ser usados por los agentes:
- `caveman-explore`: Explorador de repositorio de solo lectura.
- `caveman-review`: Revisión de código comprimida.
- `caveman-setup`: Configuración de la pasarela de Caveman.
- `caveman-optimize`: Optimización de prompts y tokens.

## Reglas de Comportamiento
1. **Brevedad**: Prefiere respuestas cortas y técnicas (estilo "caveman" si se solicita).
2. **Observabilidad**: Asegúrate de que las llamadas a LLM pasen por el gateway de Caveman si el proyecto se configura para ello.
3. **OpenSpec**: Para cambios estructurales, usa el flujo de OpenSpec (propose → review → apply → archive).
4. **Persistencia**: Actualiza este archivo o archivos similares (`CLAUDE.md`, `.cursorrules`) si hay cambios importantes en el flujo de trabajo de los agentes.
