# Walkthrough - Melhoria Visual do Seletor de Modo

Implementamos uma melhoria significativa no layout do seletor de modo da calculadora para resolver problemas de alinhamento e quebra de texto.

## Mudanças Realizadas

### Componente de Interface
- **ModeSelector.kt**:
    - Substituímos o uso de `FilterChips` por `SingleChoiceSegmentedButtonRow`.
    - Adicionamos ícones contextuais (`Schedule` para horas e `Calculate` para centesimal) para facilitar a identificação.
    - Otimizamos o estilo do texto para evitar quebras de linha que deformavam a altura dos botões.

## Resultados
O componente agora mantém uma altura fixa e uniforme, independentemente do modo selecionado. A união física dos botões no estilo segmentado passa uma ideia muito mais clara de "opções mutuamente exclusivas".

## Verificação
O aplicativo foi compilado e o deploy foi realizado com sucesso. Os componentes estão seguindo os padrões do Material Design 3.
