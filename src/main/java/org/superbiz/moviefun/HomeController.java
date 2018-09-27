package org.superbiz.moviefun;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.superbiz.moviefun.albums.Album;
import org.superbiz.moviefun.albums.AlbumFixtures;
import org.superbiz.moviefun.albums.AlbumsBean;
import org.superbiz.moviefun.movies.Movie;
import org.superbiz.moviefun.movies.MovieFixtures;
import org.superbiz.moviefun.movies.MoviesBean;

import java.util.Map;

@Controller
public class HomeController {

    private final MoviesBean moviesBean;
    private final AlbumsBean albumsBean;
    private final MovieFixtures movieFixtures;
    private final AlbumFixtures albumFixtures;
    private final PlatformTransactionManager albumsTransactionManager;
    private final PlatformTransactionManager moviesTransactionManager;

    public HomeController(PlatformTransactionManager albumsTransactionManager,PlatformTransactionManager moviesTransactionManager, MoviesBean moviesBean, AlbumsBean albumsBean, MovieFixtures movieFixtures, AlbumFixtures albumFixtures) {
        this.albumsTransactionManager = albumsTransactionManager;
        this.moviesTransactionManager=moviesTransactionManager;
        this.moviesBean = moviesBean;
        this.albumsBean = albumsBean;
        this.movieFixtures = movieFixtures;
        this.albumFixtures = albumFixtures;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/setup")
    public String setup(Map<String, Object> model) {

        AddAlbums();
        AddMovies();

        model.put("movies", moviesBean.getMovies());
        model.put("albums", albumsBean.getAlbums());

        return "setup";
    }

    private void AddMovies()
    {
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = moviesTransactionManager.getTransaction(def);

        for (Movie movie : movieFixtures.load()) {
            moviesBean.addMovie(movie);
        }
        moviesTransactionManager.commit(status);
    }

    private void AddAlbums()
    {
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = albumsTransactionManager.getTransaction(def);

        for (Album album : albumFixtures.load()) {
            albumsBean.addAlbum(album);
        }
        albumsTransactionManager.commit(status);
    }
}
