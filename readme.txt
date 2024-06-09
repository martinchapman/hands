HANDS

* Dependencies

- Git >= 2.39
- JDK (Java SE Development Kit) <= 1.8
- Apache Maven >= 3.8
- Gambit >= 16.0

* Configuration

1. Run `git submodule init`
2. Run `git submodule update`
3. Create folder `output/log`
4. Create folder `output/data`
5. Create folder `output/charts/tables` and `output/charts/figures`
6. To run a particular configuration of the software, such as winter pressures modelling, create `output/config.config` and enter 'app.plugin=winter' to use the correct configuration plugin.

* Build and Run

1. Build with `mvn clean package`
2. Execute Runner within resulting JAR, e.g. `java -cp target/hands-1.0-SNAPSHOT.jar org.kclhi.hands.utility.Runner`
