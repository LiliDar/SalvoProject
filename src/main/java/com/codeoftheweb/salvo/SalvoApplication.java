package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(PlayerRepository playerRepository, GameRepository gameRepository, GamePlayerRepository gamePlayerRepository, ShipRepository shipRepository, SalvoRepository salvoRepository, ScoreRepository scoreRepository) {
		return (args) -> {
			// save a couple of customers

            Date date = new Date();
            Date date1 = Date.from(date.toInstant());
            Date date2 = Date.from(date.toInstant().plusSeconds(3600));
            Date date3 = Date.from(date.toInstant().plusSeconds(7200));

            Game game1 = new Game();
            game1.setCreationDate(date1);

            Game game2 = new Game();
            game2.setCreationDate(date2);

            Game game3 = new Game();
            game3.setCreationDate(date3);

            Game game4 = new Game();
            game4.setCreationDate(date1);

            Game game5 = new Game();
            game5.setCreationDate(date1);

            Game game6 = new Game();
            game6.setCreationDate(date1);

            Game game7 = new Game();
            game7.setCreationDate(date1);

            Game game8 = new Game();
            game8.setCreationDate(date1);

            gameRepository.save(game1);
            gameRepository.save(game2);
            gameRepository.save(game3);
            gameRepository.save(game4);
            gameRepository.save(game5);
            gameRepository.save(game6);
            gameRepository.save(game7);
            gameRepository.save(game8);

            Player player1 = new Player("j.bauer@ctu.gov", "123");
            playerRepository.save(player1);

            Player player2 = new Player("c.obrian@ctu.gov", "123");
            playerRepository.save(player2);

            Player player3 = new Player("kim_bauer@gmail.com","123");
            playerRepository.save(player3);

            Player player4 = new Player("t.almeida@ctu.gov","123");
            playerRepository.save(player4);

            GamePlayer gamePlayer1_1 = new GamePlayer(game1, player1);
            GamePlayer gamePlayer1_2 = new GamePlayer(game1, player2);

            GamePlayer gamePlayer2_1 = new GamePlayer(game2, player1);
            GamePlayer gamePlayer2_2 = new GamePlayer(game2, player2);

            GamePlayer gamePlayer3_1 = new GamePlayer(game3, player2);
            GamePlayer gamePlayer3_2 = new GamePlayer(game3, player4);

            GamePlayer gamePlayer4_1 = new GamePlayer(game4, player2);
            GamePlayer gamePlayer4_2 = new GamePlayer(game4, player1);

            GamePlayer gamePlayer5_1 = new GamePlayer(game5, player4);
            GamePlayer gamePlayer5_2 = new GamePlayer(game5, player1);

            /*GamePlayer gamePlayer6_1 = new GamePlayer(game2, player);
            GamePlayer gamePlayer6_2 = new GamePlayer(game2, player);

            GamePlayer gamePlayer7_1 = new GamePlayer(game2, player);
            GamePlayer gamePlayer7_2 = new GamePlayer(game2, player);*/

            GamePlayer gamePlayer8_1 = new GamePlayer(game8, player3);
            GamePlayer gamePlayer8_2 = new GamePlayer(game8, player4);

            gamePlayerRepository.save(gamePlayer1_1);
            gamePlayerRepository.save(gamePlayer1_2);
            gamePlayerRepository.save(gamePlayer2_1);
            gamePlayerRepository.save(gamePlayer2_2);
            gamePlayerRepository.save(gamePlayer3_1);
            gamePlayerRepository.save(gamePlayer3_2);
            gamePlayerRepository.save(gamePlayer4_1);
            gamePlayerRepository.save(gamePlayer4_2);
            gamePlayerRepository.save(gamePlayer5_1);
            gamePlayerRepository.save(gamePlayer5_2);
            /*gamePlayerRepository.save(gamePlayer6_1);
            gamePlayerRepository.save(gamePlayer6_2);
            gamePlayerRepository.save(gamePlayer7_1);
            gamePlayerRepository.save(gamePlayer7_2);*/
            gamePlayerRepository.save(gamePlayer8_1);
            gamePlayerRepository.save(gamePlayer8_2);

            List<String> shipLocation1_1 = Arrays.asList("H2","H3", "H4");
            List<String> shipLocation1_2 = Arrays.asList("E1","F1", "G1");
            List<String> shipLocation1_3 = Arrays.asList("B4","B5");
            List<String> shipLocation1_4 = Arrays.asList("B5","C5","D5");
            List<String> shipLocation1_5 = Arrays.asList("F1","F2");

            List<String> shipLocation2_1 = Arrays.asList("B5","C5","D5");
            List<String> shipLocation2_2 = Arrays.asList("C6","C7");
            List<String> shipLocation2_3 = Arrays.asList("A2","A3","A4");
            List<String> shipLocation2_4 = Arrays.asList("G6","H6");

            List<String> salvoLocation1_1_1 = Arrays.asList("B5","C5","F1");
            List<String> salvoLocation1_1_2 = Arrays.asList("F2","D5");
            List<String> salvoLocation1_2_1 = Arrays.asList("B4","B5","B6");
            List<String> salvoLocation1_2_2 = Arrays.asList("E1","H3","A2");

            List<String> salvoLocation2_1_1 = Arrays.asList("A1","A4","G6");
            List<String> salvoLocation2_1_2 = Arrays.asList("A3","H6");
            List<String> salvoLocation2_2_1 = Arrays.asList("B5","D5","C7");
            List<String> salvoLocation2_2_2 = Arrays.asList("C5","C6");

            List<String> salvoLocation3_1_1 = Arrays.asList("G6","H6","A4");
            List<String> salvoLocation3_1_2 = Arrays.asList("A2","A3","D8");
            List<String> salvoLocation3_2_1 = Arrays.asList("H1","H2","H3");
            List<String> salvoLocation3_2_2 = Arrays.asList("E1","F2","G3");

            List<String> salvoLocation4_1_1 = Arrays.asList("A3","A4","F7");
            List<String> salvoLocation4_1_2 = Arrays.asList("A2","G6","H6");
            List<String> salvoLocation4_2_1 = Arrays.asList("B5","C6","H1");
            List<String> salvoLocation4_2_2 = Arrays.asList("C5","C7","D5");

            List<String> salvoLocation5_1_1 = Arrays.asList("A1","A2","A3");
            List<String> salvoLocation5_1_2 = Arrays.asList("G6","G7","G8");
            List<String> salvoLocation5_2_1 = Arrays.asList("B5","B6","C7");
            List<String> salvoLocation5_2_2 = Arrays.asList("C6","D6","E6");
            List<String> salvoLocation5_2_3 = Arrays.asList("H1","H8");

            Salvo salvo1_1_1 = new Salvo(1,gamePlayer1_1, salvoLocation1_1_1);
            salvoRepository.save(salvo1_1_1);
            Salvo salvo1_1_2 = new Salvo(2,gamePlayer1_1, salvoLocation1_1_2);
            salvoRepository.save(salvo1_1_2);

            Salvo salvo1_2_1 = new Salvo(1,gamePlayer1_2, salvoLocation1_2_1);
            salvoRepository.save(salvo1_2_1);
            Salvo salvo1_2_2 = new Salvo(2,gamePlayer1_2, salvoLocation1_2_2);
            salvoRepository.save(salvo1_2_2);

            Salvo salvo2_1_1 = new Salvo(1,gamePlayer2_1, salvoLocation2_1_1);
            salvoRepository.save(salvo2_1_1);
            Salvo salvo2_1_2 = new Salvo(2,gamePlayer2_1, salvoLocation2_1_2);
            salvoRepository.save(salvo2_1_2);

            Salvo salvo2_2_1 = new Salvo(1,gamePlayer2_2, salvoLocation2_2_1);
            salvoRepository.save(salvo2_2_1);
            Salvo salvo2_2_2 = new Salvo(2,gamePlayer2_2, salvoLocation2_2_2);
            salvoRepository.save(salvo2_2_2);

            Salvo salvo3_1_1 = new Salvo(1,gamePlayer3_1, salvoLocation3_1_1);
            salvoRepository.save(salvo3_1_1);
            Salvo salvo3_1_2 = new Salvo(2,gamePlayer3_1, salvoLocation3_1_2);
            salvoRepository.save(salvo3_1_2);

            Salvo salvo3_2_1 = new Salvo(1,gamePlayer3_2, salvoLocation3_2_1);
            salvoRepository.save(salvo3_2_1);
            Salvo salvo3_2_2 = new Salvo(2,gamePlayer3_2, salvoLocation3_2_2);
            salvoRepository.save(salvo3_2_2);

            Salvo salvo4_1_1 = new Salvo(1,gamePlayer4_1, salvoLocation4_1_1);
            salvoRepository.save(salvo4_1_1);
            Salvo salvo4_1_2 = new Salvo(2,gamePlayer4_1, salvoLocation4_1_2);
            salvoRepository.save(salvo4_1_2);

            Salvo salvo4_2_1 = new Salvo(1,gamePlayer4_2, salvoLocation4_2_1);
            salvoRepository.save(salvo4_2_1);
            Salvo salvo4_2_2 = new Salvo(2,gamePlayer4_2, salvoLocation4_2_2);
            salvoRepository.save(salvo4_2_2);

            Salvo salvo5_1_1 = new Salvo(1,gamePlayer5_1, salvoLocation5_1_1);
            salvoRepository.save(salvo5_1_1);
            Salvo salvo5_1_2 = new Salvo(2,gamePlayer5_1, salvoLocation5_1_2);
            salvoRepository.save(salvo5_1_2);

            Salvo salvo5_2_1 = new Salvo(1,gamePlayer5_2, salvoLocation5_2_1);
            salvoRepository.save(salvo5_2_1);
            Salvo salvo5_2_2 = new Salvo(2,gamePlayer5_2, salvoLocation5_2_2);
            salvoRepository.save(salvo5_2_2);
            Salvo salvo5_2_3 = new Salvo(3,gamePlayer5_2, salvoLocation5_2_3);
            salvoRepository.save(salvo5_2_3);




            Ship ship1_1 = new Ship("Destroyer", shipLocation1_1 ,gamePlayer1_1);
            shipRepository.save(ship1_1);

            Ship ship1_2 = new Ship("Submarine", shipLocation1_2, gamePlayer1_1);
            shipRepository.save(ship1_2);

            Ship ship1_3 = new Ship("Patrol Boat", shipLocation1_3, gamePlayer1_1);
            shipRepository.save(ship1_3);

            Ship ship1_4 = new Ship("Destroyer", shipLocation1_4, gamePlayer1_2);
            shipRepository.save(ship1_4);

            Ship ship1_5 = new Ship("Patrol Boat", shipLocation1_5, gamePlayer1_2);
            shipRepository.save(ship1_5);


            Ship ship2_1 = new Ship("Destroyer", shipLocation2_1 ,gamePlayer2_1);
            shipRepository.save(ship2_1);

            Ship ship2_2 = new Ship("Submarine", shipLocation2_2, gamePlayer2_1);
            shipRepository.save(ship2_2);

            Ship ship2_3 = new Ship("Patrol Boat", shipLocation2_3, gamePlayer2_2);
            shipRepository.save(ship2_3);

            Ship ship2_4 = new Ship("Destroyer", shipLocation2_4, gamePlayer2_2);
            shipRepository.save(ship2_4);


            Ship ship3_1 = new Ship("Destroyer", shipLocation2_1 ,gamePlayer3_1);
            shipRepository.save(ship3_1);

            Ship ship3_2 = new Ship("Submarine", shipLocation2_2, gamePlayer3_1);
            shipRepository.save(ship3_2);

            Ship ship3_3 = new Ship("Patrol Boat", shipLocation2_3, gamePlayer3_2);
            shipRepository.save(ship3_3);

            Ship ship3_4 = new Ship("Destroyer", shipLocation2_4, gamePlayer3_2);
            shipRepository.save(ship3_4);


            Ship ship4_1 = new Ship("Destroyer", shipLocation2_1 ,gamePlayer4_1);
            shipRepository.save(ship4_1);

            Ship ship4_2 = new Ship("Submarine", shipLocation2_2, gamePlayer4_1);
            shipRepository.save(ship4_2);

            Ship ship4_3 = new Ship("Patrol Boat", shipLocation2_3, gamePlayer4_2);
            shipRepository.save(ship4_3);

            Ship ship4_4 = new Ship("Destroyer", shipLocation2_4, gamePlayer4_2);
            shipRepository.save(ship4_4);


            Ship ship5_1 = new Ship("Destroyer", shipLocation2_1 ,gamePlayer5_1);
            shipRepository.save(ship5_1);

            Ship ship5_2 = new Ship("Submarine", shipLocation2_2, gamePlayer5_1);
            shipRepository.save(ship5_2);

            Ship ship5_3 = new Ship("Patrol Boat", shipLocation2_3, gamePlayer5_2);
            shipRepository.save(ship5_3);

            Ship ship5_4 = new Ship("Destroyer", shipLocation2_4, gamePlayer5_2);
            shipRepository.save(ship5_4);


            Ship ship8_1 = new Ship("Destroyer", shipLocation2_1 ,gamePlayer8_1);
            shipRepository.save(ship8_1);

            Ship ship8_2 = new Ship("Submarine", shipLocation2_2, gamePlayer8_1);
            shipRepository.save(ship8_2);

            Ship ship8_3 = new Ship("Patrol Boat", shipLocation2_3, gamePlayer8_2);
            shipRepository.save(ship8_3);

            Ship ship8_4 = new Ship("Destroyer", shipLocation2_4, gamePlayer8_2);
            shipRepository.save(ship8_4);

            Score score1_1 = new Score(game1, player1, 1.0);
            Score score1_2 = new Score(game1, player2, 0.0);

            Score score2_1 = new Score(game2, player1, 0.5);
            Score score2_2 = new Score(game2, player2, 0.5);

            Score score3_1 = new Score(game3, player2, 1.0);
            Score score3_2 = new Score(game3, player4, 1.0);

            Score score4_1 = new Score(game4, player1, 0.5);
            Score score4_2 = new Score(game4, player2, 0.5);

            scoreRepository.save(score1_1);
            scoreRepository.save(score1_2);
            scoreRepository.save(score2_1);
            scoreRepository.save(score2_2);
            scoreRepository.save(score3_1);
            scoreRepository.save(score3_2);
            scoreRepository.save(score4_1);
            scoreRepository.save(score4_2);

		};
    }
}


@Configuration
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {

    @Autowired
    PlayerRepository playerRepository;

    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(inputName-> {
            Player player = playerRepository.findByEmail(inputName).get(0);
            if (player != null) {
                return new User(player.getEmail(), player.getPassword(),
                        AuthorityUtils.createAuthorityList("USER"));
            } else {
                throw new UsernameNotFoundException("Unknown user: " + inputName);
            }
        });
    }
}

@Configuration
@EnableWebSecurity
class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/web/games.html").permitAll()
                .antMatchers("/web/game.css").permitAll()
                .antMatchers("/web/games.js").permitAll()
                .antMatchers("/web/images/**").permitAll()
                .antMatchers("/api/login").permitAll()
                .antMatchers("/api/leader-board").permitAll()
                .antMatchers("/rest/**").denyAll()
                .anyRequest().fullyAuthenticated()
                .and()
                .formLogin();

        http.formLogin()
                .usernameParameter("email")
                .passwordParameter("password")
                .loginPage("/api/login");

        http.logout().logoutUrl("/api/logout");

        // turn off checking for CSRF tokens
        http.csrf().disable();

        // if user is not authenticated, just send an authentication failure response
        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if login is successful, just clear the flags asking for authentication
        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

        // if login fails, just send an authentication failure response
        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if logout is successful, just send a success response
        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }

    }

}
