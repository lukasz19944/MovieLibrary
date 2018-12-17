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
import library.dao.MovieDao;
import library.model.Actor;
import library.model.Director;
import library.model.Movie;
import library.view.*;

import java.io.IOException;

public class MainApp extends Application {
    private Stage primaryStage;
    private BorderPane rootLayout;

    private ObservableList<Movie> movieData = FXCollections.observableArrayList();

    public MainApp() {
        MovieDao dao = new MovieDao();

        movieData.addAll(dao.getAllMovies());
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Movie Library");

        initRootLayout();

        showMovieOverview();
    }

    public void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
            rootLayout = loader.load();

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

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
            loader.setLocation(MainApp.class.getResource("view/MovieEditDialog.fxml"));
            AnchorPane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Movie");
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
            loader.setLocation(MainApp.class.getResource("view/NewDirectorDialog.fxml"));
            AnchorPane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add Director");
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

    public boolean showEditActorsDialog(Movie movie) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/EditActorsDialog.fxml"));
            AnchorPane page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Actors");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            EditActorsDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setMovie(movie);
            controller.setMainApp(this);

            dialogStage.showAndWait();

            return controller.isSaveClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean showActorEditDialog(Actor actor, Movie movie) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/ActorEditDialog.fxml"));
            AnchorPane page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Actor");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            ActorEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setMovie(movie);
            controller.setActor(actor);
            controller.setMainApp(this);

            if (actor.getFirstName() == null) {
                controller.setActorExist(false);
            } else {
                controller.setActorExist(true);
            }

            dialogStage.showAndWait();

            return controller.isSaveClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public ObservableList<Movie> getMovieData() {
        return movieData;
    }
}
