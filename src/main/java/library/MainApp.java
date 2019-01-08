package library;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import library.dao.ActorDao;
import library.dao.DirectorDao;
import library.dao.MovieActorDao;
import library.dao.MovieDao;
import library.model.Actor;
import library.model.Director;
import library.model.Movie;
import library.model.MovieActor;
import library.view.*;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class MainApp extends Application {
    private Stage primaryStage;
    private BorderPane rootLayout;

    private ResourceBundle bundle;

    private Locale locale = Locale.ENGLISH;

    private ObservableList<Movie> movieData = FXCollections.observableArrayList();
    private ObservableList<Director> directorData = FXCollections.observableArrayList();
    private ObservableList<Actor> actorData = FXCollections.observableArrayList();
    private ObservableList<MovieActor> maleRoleData = FXCollections.observableArrayList();
    private ObservableList<MovieActor> femaleRoleData = FXCollections.observableArrayList();

    public MainApp() {
        MovieDao mdao = new MovieDao();
        DirectorDao dDao = new DirectorDao();
        ActorDao aDao = new ActorDao();
        MovieActorDao maDao = new MovieActorDao();

        movieData.addAll(mdao.getAllMovies());
        directorData.addAll(dDao.getAllDirectors());
        actorData.addAll(aDao.getAllActors());
        maleRoleData.addAll(maDao.getBestRoles("Male").subList(0, 10));
        femaleRoleData.addAll(maDao.getBestRoles("Female").subList(0, 10));
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        initRootLayout();

        showMovieOverview();
    }

    public void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            bundle = ResourceBundle.getBundle("bundles/messages", locale);
            loader.setResources(bundle);
            loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
            rootLayout = loader.load();

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            primaryStage.setTitle(bundle.getString("application_title"));

            RootLayoutController controller = loader.getController();
            controller.setMainApp(this);

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showMovieOverview() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setResources(bundle);
            loader.setLocation(MainApp.class.getResource("view/MovieOverview.fxml"));
            AnchorPane movieOverview = loader.load();

            rootLayout.setCenter(movieOverview);

            MovieOverviewController controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public boolean showMovieEditDialog(Movie movie) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setResources(bundle);
            loader.setLocation(MainApp.class.getResource("view/MovieEditDialog.fxml"));
            AnchorPane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle(bundle.getString("movie_edit_title"));
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            MovieEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setMovie(movie);
            controller.setMainApp(this);

            if (movie.getTitle() == null) {
                controller.setMovieExist(false);
            } else {
                controller.setMovieExist(true);
            }

            dialogStage.showAndWait();

            return controller.isSaveClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean showNewDirectorDialog(Director director) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setResources(bundle);
            loader.setLocation(MainApp.class.getResource("view/NewDirectorDialog.fxml"));
            AnchorPane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle(bundle.getString("director_add_title"));
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            NewDirectorDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setDirector(director);
            controller.setMainApp(this);

            dialogStage.showAndWait();

            return controller.isSaveClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean showNewActorDialog(Actor actor) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setResources(bundle);
            loader.setLocation(MainApp.class.getResource("view/NewActorDialog.fxml"));
            AnchorPane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle(bundle.getString("actor_add_title"));
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            NewActorDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setActor(actor);
            controller.setMainApp(this);

            dialogStage.showAndWait();

            return controller.isSaveClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void showMovieStatistics() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setResources(bundle);
            loader.setLocation(MainApp.class.getResource("view/MovieStatisticsDialog.fxml"));
            AnchorPane page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle(bundle.getString("movie_stats_title"));
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            MovieStatisticsDialogController controller = loader.getController();
            controller.setMainApp(this);

            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showDirectorStatistics() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setResources(bundle);
            loader.setLocation(MainApp.class.getResource("view/DirectorStatisticsDialog.fxml"));
            AnchorPane page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle(bundle.getString("director_stats_title"));
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            DirectorStatisticsDialogController controller = loader.getController();
            controller.setMainApp(this);

            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showActorStatistics() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setResources(bundle);
            loader.setLocation(MainApp.class.getResource("view/ActorStatisticsDialog.fxml"));
            AnchorPane page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle(bundle.getString("actor_stats_title"));
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            ActorStatisticsDialogController controller = loader.getController();
            controller.setMainApp(this);

            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showBestRolesDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setResources(bundle);
            loader.setLocation(MainApp.class.getResource("view/BestRolesDialog.fxml"));
            AnchorPane page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle(bundle.getString("best_roles_title"));
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            BestRolesDialogController controller = loader.getController();
            controller.setMainApp(this);

            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void changeLanguagePolish() {
        this.locale = new Locale("pl", "PL");
    }

    public void changeLanguageEnglish() {
        this.locale = new Locale("en", "GB");
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public ObservableList<Movie> getMovieData() {
        return movieData;
    }

    public ObservableList<Director> getDirectorData() {
        return directorData;
    }

    public ObservableList<Actor> getActorData() {
        return actorData;
    }

    public ObservableList<MovieActor> getMaleRoleData() {
        return maleRoleData;
    }

    public ObservableList<MovieActor> getFemaleRoleData() {
        return femaleRoleData;
    }
}
