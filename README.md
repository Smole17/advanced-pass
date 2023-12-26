# Important information
Level increases every 100 xp (levels = xp / 100). Level limit is levels pool size.

# Begging
- Firstly, you need to install ORMLite (6.1) to use this in your workspace.
- Secondly, setup my project as dependency using:
## Maven:
```xml
<repositories>
    <repository>
        <id>Maven GitHub Repository</id>
        <url>"https://maven.pkg.github.com/Smole17/*"</url>
    </repository>
</repositories>
```
```xml
<dependency>
    <groupId>ru.smole.advancedpass</groupId>
    <artifactId>advancedpass</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```
## Gradle:
```gradle
repositories {
    maven {
        url = "https://maven.pkg.github.com/Smole17/*"
        credentials {
            username = System.getenv("GITHUB_ACTOR")
            password = System.getenv("GITHUB_TOKEN")
        }
    }
}
```
```gradle
dependencies {
    compileOnly "ru.smole.advancedpass:advancedpass:1.0-SNAPSHOT"
}
```
- In final initialize system using ``AdvancedPass.initialize(jdbcUrl, username, password, initPlayerLoader (boolean))`` while plugin is enabling.

# How to use?
> Add some levels in pool (ordinal is level)
```java
AdvancedPass.addPassLevel(SimplePassLevel.builder()
        .ordinal(1)
        .freeRewardConsumer(passHolder -> {
            val player = Bukkit.getPlayer(passHolder.getUniqueId());

            player.sendMessage("You had take free reward! Nice!!");
        })
        .premiumRewardConsumer(passHolder -> {
            val player = Bukkit.getPlayer(passHolder.getUniqueId());

            player.sendMessage("OOOOOH!! You are rich and you had take premium reward! That's awesome!!!");
            player.getInventory()
                    .addItem(new ItemStack(Material.DIAMOND, 5));
        })
        .build()
);
        
AdvancedPass.addPassLevel(SimplePassLevel.builder()
        .ordinal(2)
        .freeRewardConsumer(passHolder -> {
            val player = Bukkit.getPlayer(passHolder.getUniqueId());

            player.sendMessage("You had take free reward from second level!");
        })
        .build()
);
```
> Add some quests in pool
```java
AdvancedPass.addPassQuest(SimplePassQuest.builder()
        .id("break_dirt")
        .neededAmount(5)
        .expReward(20)
        .build()
);

AdvancedPass.addPassQuest(SimplePassQuest.builder()
        .id("break_anything")
        .neededAmount(100)
        .expReward(10)
        .build()
);
```
> Pin quests to holder (f.e. to player on every join)
```java
@EventHandler
public void onJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();

    AdvancedPass.getPassHolder(player.getUniqueId())
            .ifPresent(passHolder -> {
                passHolder.addQuest("break_dirt");
                passHolder.addQuest("break_stone");
            });
}
```
> Increase quests progress
```java
@EventHandler
public void onBreak(BlockBreakEvent event) {
    Player player = event.getPlayer();
    Optional<PassHolder> optionalPassHolder = AdvancedPass.getPassHolder(player.getUniqueId());

    if (optionalPassHolder.isEmpty()) return;

    PassHolder passHolder = optionalPassHolder.get();

    passHolder.addQuestProgress("break_anything", 1);

    Material type = event.getBlock().getType();

    if (type != Material.DIRT) return;

    passHolder.addQuestProgress("break_dirt", 1);
}
```
> And make your gui to receive battlepass rewards :)
