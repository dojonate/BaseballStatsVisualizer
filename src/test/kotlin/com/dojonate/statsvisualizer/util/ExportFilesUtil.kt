import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

fun main() {
    val files = listOf(
        "src/main/java/com/dojonate/statsvisualizer/controller/EventUploadController.java",
        "src/main/java/com/dojonate/statsvisualizer/controller/PlayersViewController.java",
        "src/main/java/com/dojonate/statsvisualizer/controller/RostersViewController.java",
        "src/main/java/com/dojonate/statsvisualizer/controller/RosterUploadController.java",
        "src/main/java/com/dojonate/statsvisualizer/controller/SampleController.java",
        "src/main/java/com/dojonate/statsvisualizer/controller/TeamsViewController.java",
        "src/main/java/com/dojonate/statsvisualizer/controller/TeamUploadController.java",
        "src/main/java/com/dojonate/statsvisualizer/model/Game.java",
        "src/main/java/com/dojonate/statsvisualizer/model/Player.java",
        "src/main/java/com/dojonate/statsvisualizer/model/PlayerEvent.java",
        "src/main/java/com/dojonate/statsvisualizer/model/Position.java",
        "src/main/java/com/dojonate/statsvisualizer/model/RosterEntry.java",
        "src/main/java/com/dojonate/statsvisualizer/model/RunnerAdvance.java",
        "src/main/java/com/dojonate/statsvisualizer/model/Team.java",
        "src/main/java/com/dojonate/statsvisualizer/repository/GameRepository.java",
        "src/main/java/com/dojonate/statsvisualizer/repository/PlayerEventRepository.java",
        "src/main/java/com/dojonate/statsvisualizer/repository/PlayerRepository.java",
        "src/main/java/com/dojonate/statsvisualizer/repository/RosterEntryRepository.java",
        "src/main/java/com/dojonate/statsvisualizer/repository/TeamRepository.java",
        "src/main/java/com/dojonate/statsvisualizer/service/GameService.java",
        "src/main/java/com/dojonate/statsvisualizer/service/PlayerEventService.java",
        "src/main/java/com/dojonate/statsvisualizer/service/PlayerService.java",
        "src/main/java/com/dojonate/statsvisualizer/service/RosterEntryService.java",
        "src/main/java/com/dojonate/statsvisualizer/service/TeamService.java",
        "src/main/java/com/dojonate/statsvisualizer/util/CsvTeamParser.java",
        "src/main/java/com/dojonate/statsvisualizer/util/EventDetails.java",
        "src/main/java/com/dojonate/statsvisualizer/util/EventFileParser.java",
        "src/main/java/com/dojonate/statsvisualizer/util/RosFileParser.java",
        "src/main/java/com/dojonate/statsvisualizer/StatsVisualizerApplication.java",
        "src/main/resources/templates/fragments/footer.html",
        "src/main/resources/templates/fragments/header.html",
        "src/main/resources/templates/fragments/pagination.html",
        "src/main/resources/templates/fragments/styles-scripts.html",
        "src/main/resources/templates/index.html",
        "src/main/resources/templates/players.html",
        "src/main/resources/templates/rosters.html",
        "src/main/resources/templates/teams.html",
        "src/test/kotlin/com/dojonate/statsvisualizer/controller/PlayersViewControllerTest.kt",
        "src/test/kotlin/com/dojonate/statsvisualizer/controller/RostersViewControllerTest.kt",
        "src/test/kotlin/com/dojonate/statsvisualizer/controller/RosterUploadControllerTest.kt",
        "src/test/kotlin/com/dojonate/statsvisualizer/controller/TeamsViewControllerTest.kt",
        "src/test/kotlin/com/dojonate/statsvisualizer/controller/TeamUploadControllerTest.kt",
        "src/test/kotlin/com/dojonate/statsvisualizer/service/PlayerServiceTest.kt",
        "src/test/kotlin/com/dojonate/statsvisualizer/service/RosterEntryServiceTest.kt",
        "src/test/kotlin/com/dojonate/statsvisualizer/service/TeamServiceTest.kt",
        "src/test/kotlin/com/dojonate/statsvisualizer/util/RosFileParserTest.kt",
        "src/test/kotlin/com/dojonate/statsvisualizer/util/EventFileParserTest.kt",
        "src/test/kotlin/com/dojonate/statsvisualizer/util/CsvTeamParserTest.kt",
        "src/test/kotlin/com/dojonate/statsvisualizer/StatsVisualizerApplicationTests.kt",
        "src/test/resources/application-test.properties",
        "build.gradle"
    )

    val outputFile = Paths.get("consolidated_file.txt")
    Files.deleteIfExists(outputFile)
    Files.createFile(outputFile)

    files.forEach { file ->
        val content = Files.readString(Paths.get(file))
        val header = "==== START OF $file ====\n"
        val footer = "\n==== END OF $file ====\n"
        Files.writeString(outputFile, header, StandardOpenOption.APPEND)
        Files.writeString(outputFile, content, StandardOpenOption.APPEND)
        Files.writeString(outputFile, footer, StandardOpenOption.APPEND)
    }
}