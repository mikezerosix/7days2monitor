package org.mikezerosix;

import org.mikezerosix.comet.CometSharedMessageQueue;
import org.mikezerosix.entities.*;
import org.mikezerosix.ftp.FTPService;
import org.mikezerosix.rest.*;
import org.mikezerosix.service.*;
import org.mikezerosix.telnet.TelnetRunner;
import org.mikezerosix.telnet.commands.ListPlayersRepeatingCommand;
import org.mikezerosix.telnet.handlers.ChatHandler;
import org.mikezerosix.telnet.handlers.PlayerLoginHandler;
import org.mikezerosix.telnet.handlers.StatHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.sql.SQLException;

@Configuration
@ComponentScan
@EnableJpaRepositories
@EnableTransactionManagement

@Import({JdbcConfiguration.class})
public class AppConfiguration {

    @Inject
    private SettingsRepository settingsRepository;

    @Inject
    private ConnectionRepository connectionRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private PlayerRepository playerRepository;

    @Inject
    private StatRepository statRepository;

    @PostConstruct
    public void init() throws SQLException {
        LogConfiguration.init();
    }

    @Bean
    public CometSharedMessageQueue cometSharedMessageQueue() {
        return new CometSharedMessageQueue();
    }

    @Bean
    public TelnetRunner telnetRunner(CometSharedMessageQueue cometSharedMessageQueue) {
        return new TelnetRunner(cometSharedMessageQueue);
    }

    @Bean
    public FTPService ftpService(CometSharedMessageQueue cometSharedMessageQueue) {
        return new FTPService(cometSharedMessageQueue);
    }


    @Bean
    public SettingsService settingsService(SettingsRepository settingsRepository, ConnectionRepository connectionRepository, MonitoringService monitoringService) {
        return new SettingsService(settingsRepository, connectionRepository, monitoringService);
    }

    @Bean
    public UserService userService(UserRepository userRepository) {
        return new UserService(userRepository);
    }

    @Bean
    public StatService statService(CometSharedMessageQueue cometSharedMessageQueue, StatRepository statRepository) {
        return new StatService(statRepository, cometSharedMessageQueue);
    }

    //TODO: maybe add handlerFactory class and pass it to this ?
    @Bean
    public MonitoringService monitoringService(TelnetRunner telnetRunner, FTPService ftpService, StatHandler statHandler, ChatHandler chatHandler, PlayerLoginHandler playerLoginHandler, ListPlayersRepeatingCommand listPlayersRepeatingCommand) {
        return new MonitoringService(telnetRunner, ftpService, statHandler, chatHandler, playerLoginHandler, listPlayersRepeatingCommand);
    }

    @Bean
    public ChatService chatService(CometSharedMessageQueue cometSharedMessageQueue) {
        return new ChatService(cometSharedMessageQueue);
    }
    @Bean
    public PlayerService playerService(CometSharedMessageQueue cometSharedMessageQueue, PlayerRepository playerRepository) {
        return new PlayerService(cometSharedMessageQueue,playerRepository);
    }

    /* Handlers   */
    @Bean
    public StatHandler statHandler(StatService statService) {
        return new StatHandler(statService);
    }

    @Bean
    public ChatHandler chatHandler(ChatService chatService) {
        return new ChatHandler(chatService);
    }

    @Bean
    public PlayerLoginHandler playerLoginHandler(PlayerService playerService) {
        return new PlayerLoginHandler( playerService);
    }

    @Bean
    public ListPlayersRepeatingCommand listPlayersRepeatingCommand(PlayerService playerService) {
        return new ListPlayersRepeatingCommand(playerService);
    }

    /* Routes */
    //TODO: change to use monitoringService

    @Bean
    public SettingsResource settingsResource(SettingsService settingsService) {
        return new SettingsResource(settingsService);
    }

    @Bean
    public LoginResource loginResource(UserRepository userRepository) {
        return new LoginResource(userRepository);
    }

    @Bean
    public UserResource userResource(UserRepository userRepository) {
        return new UserResource(userRepository);
    }

    @Bean
    public TelnetResource telnetResource(TelnetRunner telnetRunner) {
        return new TelnetResource(telnetRunner);
    }

    @Bean
    public ServerResource serverResource() {
        return new ServerResource();
    }

    @Bean
    public StatResource statResource() {
        return new StatResource(statRepository);
    }

    @Bean
    public ChatResource chatResource(TelnetRunner telnetRunner, ChatService chatService) {
        return new ChatResource(telnetRunner, chatService);
    }

    @Bean
    public PlayerResource playerResource(MonitoringService monitoringService) {
        return new PlayerResource(monitoringService, playerRepository);
    }

    @Bean
    public FTPResource ftpResource(FTPService ftpService) {
        return new FTPResource(ftpService);
    }

    @Bean
    public CometResource cometResource(CometSharedMessageQueue cometSharedMessageQueue) {
        return new CometResource(cometSharedMessageQueue);
    }
}