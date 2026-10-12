# Calculadora de Horas & Centesimal para Android

Aplicativo Android nativo desenvolvido em **Kotlin** e **Jetpack Compose** (Material Design 3) para cálculos de horas em formato tradicional (Sexagesimal: `HH:MM`) e conversão em tempo real para Horas Centesimais (Decimais: `0,00 h`).

---

## Funcionalidades Principais

1. **Cálculo Sexagesimal e Centesimal**:
   - Operações básicas: Adição (`+`), Subtração (`-`), Multiplicação (`×`) e Divisão (`÷`).
   - Exemplo: `00:30 (0,50 h) + 00:30 (0,50 h) = 01:00 (1,00 h)`.
2. **Cálculo Bidirecional (Inverso)**:
   - **Modo HH:MM → Centesimal**: Digitação em horas e minutos, exibindo a conversão centesimal em tempo real.
   - **Modo Centesimal → HH:MM**: Digitação em valores decimais (`1,50`), exibindo a conversão para horas e minutos (`01:30`) instantaneamente.
3. **Máscara Inteligente de Digitação**:
   - Digitação sequencial rápida sem necessidade de pontuação manual (ex: digitar `130` $\rightarrow$ `01:30`).
4. **Atalhos Rápidos**:
   - Botões de incremento rápido: `+15m` e `+30m`.
5. **Histórico de Operações**:
   - Armazenamento dos cálculos realizados com timestamp e recuperação do resultado para novo cálculo.

---

## Arquitetura e Tecnologias

O projeto segue as diretrizes recomendadas pelo Google de **Clean Architecture** e **UDF (Unidirectional Data Flow)**:

- **Linguagem**: Kotlin 2.0
- **UI Framework**: Jetpack Compose com Material Design 3 (Material You)
- **Gerenciamento de Estado**: Android ViewModel + Kotlin Coroutines + `StateFlow`
- **Camada de Domínio (`domain`)**:
  - `TimeSpan`: Modelo imutável de duração com precisão matemática.
  - `TimeConverter`: Conversor puro entre sexagesimal e centesimal.
  - `TimeInputMask`: Gerenciador de digitação e formatação.
  - `ExpressionEvaluator`: Motor de avaliação aritmética.
- **Camada de Apresentação (`presentation`)**:
  - `CalculatorScreen`: Tela principal com Scaffold e TopAppBar.
  - `DisplaySection`: Visor duplo moderno com badge centesimal.
  - `KeypadSection`: Grid de teclas responsivas com animações e cores contextuais.
  - `HistoryBottomSheet`: Painel deslizante com fita de histórico.
- **Testes Unitários**: JUnit 4/5 cobrindo 100% das regras de cálculo e estados da ViewModel.

---

## Estrutura de Pastas

```
app/src/main/java/com/example/hourcalculator/
├── domain/
│   ├── model/
│   │   ├── CalculatorMode.kt       # Enum dos modos de conversão
│   │   ├── Operator.kt             # Operadores aritméticos (+, -, ×, ÷)
│   │   ├── TimeSpan.kt             # Modelo de tempo em minutos
│   │   └── CalculationRecord.kt    # Modelo do histórico
│   └── engine/
│       ├── TimeConverter.kt        # Conversão pura HH:MM <-> Decimal
│       ├── TimeInputMask.kt        # Máscara inteligente
│       └── ExpressionEvaluator.kt  # Avaliador de expressões
├── data/
│   └── repository/
│       ├── HistoryRepository.kt    # Interface do repositório
│       └── InMemoryHistoryRepository.kt
├── presentation/
│   ├── calculator/
│   │   ├── CalculatorUiState.kt    # Estado imutável da UI
│   │   ├── CalculatorUiEvent.kt    # Eventos da UI
│   │   ├── CalculatorViewModel.kt  # Gerenciador de estado
│   │   ├── CalculatorScreen.kt     # Composable principal
│   │   └── components/
│   │       ├── CalculatorButton.kt # Botão estilizado
│   │       ├── ModeSelector.kt     # Seletor de modo
│   │       ├── DisplaySection.kt   # Visor com badge
│   │       └── KeypadSection.kt    # Grid de botões
│   └── history/
│       ├── HistoryBottomSheet.kt   # Painel de histórico
│       └── HistoryItemCard.kt      # Card de item do histórico
└── ui/theme/
    ├── Color.kt, Theme.kt, Type.kt # Tema Material 3
```

---

## Como Abrir e Executar no Android Studio

1. Abra o **Android Studio**.
2. Selecione **File > Open** e navegue até a pasta `c:\Projetos\HourCalculator`.
3. Aguarde o Gradle sincronizar as dependências.
4. Escolha um emulador ou conecte seu smartphone via USB com a Depuração USB ativada.
5. Clique no botão **Run (▶)** para compilar e instalar o app!
