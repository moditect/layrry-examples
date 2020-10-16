package build;

import de.sormuras.bach.Bach;
import de.sormuras.bach.Configuration;
import de.sormuras.bach.Project;
import de.sormuras.bach.project.Feature;
import de.sormuras.bach.project.Link;
import java.util.Locale;

public class Build {

  public static void main(String... args) {
    var configuration = Configuration.ofSystem();
    var project =
        Project.ofCurrentDirectory()
            .without(Feature.CREATE_API_DOCUMENTATION)
            // Moditect
            .with(
                Link.of(
                    "org.moditect.layrry.platform",
                    "https://jitpack.io/com/github/moditect/layrry/layrry-platform/master-38dc0e476a-1/layrry-platform-master-38dc0e476a-1.jar"))
            // JavaFX
            .with(linkModuleOfJavaFX("base"))
            .with(linkModuleOfJavaFX("controls"))
            .with(linkModuleOfJavaFX("fxml"))
            .with(linkModuleOfJavaFX("graphics"))
            .with(linkModuleOfJavaFX("media"))
            .with(linkModuleOfJavaFX("swing"))
            .with(linkModuleOfJavaFX("web"));

    new Bach(configuration, project).build();
  }

  // https://repo.maven.apache.org/maven2/org/openjfx
  static Link linkModuleOfJavaFX(String suffix) {
    var group = "org.openjfx";
    var version = "15";
    var platform = of("linux", "mac", "win");
    var coordinates = String.join(":", group, "javafx-" + suffix, version, platform);
    return Link.ofCentral("javafx." + suffix, coordinates);
  }

  static <T> T of(T linux, T mac, T windows) {
    var os = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);
    if (os.contains("win")) return windows;
    if (os.contains("mac")) return mac;
    return linux;
  }
}
