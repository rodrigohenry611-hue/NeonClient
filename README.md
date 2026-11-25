# Neon Client (Fabric 1.21.11)

Mod client-side com menu (Right Shift), cosméticos, view model e swing.

## Como rodar
1. Instale o JDK 21.
2. Gere o projeto-base em https://fabricmc.net/develop/template (1.21.11, Yarn, sem data generation)
   e copie para ele a pasta `src/` deste zip (substituindo os arquivos), além do `gradle.properties` se quiser.
   (Este zip não inclui o gradle wrapper.)
3. `./gradlew runClient` para testar, `./gradlew build` para gerar o .jar em build/libs.

## Estrutura
- `cosmetics/Cosmetics.java` – lista dos cosméticos (adicione novos aqui)
- `cosmetics/CosmeticFeatureRenderer.java` – desenha os cosméticos no jogador
- `mixin/HeldItemRendererMixin.java` – view model + estilos de swing
- `mixin/LivingEntityMixin.java` – velocidade do swing
- `NeonViewModelScreen.java`, `NeonCosmeticsScreen.java`, `NeonMenuScreen.java` – telas
