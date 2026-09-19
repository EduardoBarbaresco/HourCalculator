# Melhoria Visual do Seletor de Modo

Este plano visa corrigir a quebra de layout e o desalinhamento visual nos botões de conversão (Horas <-> Centesimal). Substituiremos os `FilterChips` individuais por um `SegmentedButtonRow` do Material Design 3, que é o componente padrão para seleções exclusivas, garantindo altura uniforme e melhor alinhamento.

## User Review Required

> [!NOTE]
> A mudança para `SegmentedButton` altera levemente a estética de "botões soltos" para um componente único segmentado. Isso é considerado uma melhoria de usabilidade e consistência visual no Android moderno.

## Proposed Changes

### Presentation Layer

#### [MODIFY] ModeSelector.kt
- Substituir `Row` + `FilterChip` por `SingleChoiceSegmentedButtonRow`.
- Implementar `SegmentedButton` para cada entrada de `CalculatorMode`.
- Adicionar ícones de direção (`Sync` ou `Swap`) para reforçar o conceito de conversão visualmente.
- Ajustar o estilo do texto para garantir que ele caiba em telas menores sem quebrar de forma irregular.

## Verification Plan

### Manual Verification
1.  **Deploy no celular**: Verificar se o componente aparece alinhado e com altura fixa em ambos os modos.
2.  **Alternância de Modos**: Clicar em cada modo e observar se a transição visual é suave e se o "check" ou destaque visual está claro.
3.  **Visualização Horizontal/Vertical**: Garantir que o componente preencha a largura da tela de forma equilibrada.
