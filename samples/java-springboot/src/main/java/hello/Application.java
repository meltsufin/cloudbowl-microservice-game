package hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Random;

@SpringBootApplication
@RestController
public class Application {
	private final static int RANGE = 3;

  static class Self {
    public String href;
  }

  static class Links {
    public Self self;
  }

  static class PlayerState {
    public Integer x;
    public Integer y;
    public String direction;
    public Boolean wasHit;
    public Integer score;
  }

  static class Arena {
    public List<Integer> dims;
    public Map<String, PlayerState> state;
  }

  static class ArenaUpdate {
    public Links _links;
    public Arena arena;
  }

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @InitBinder
  public void initBinder(WebDataBinder binder) {
    binder.initDirectFieldAccess();
  }

  @GetMapping("/")
  public String index() {
    return "Let the battle begin!!!";
  }

  @PostMapping("/**")
  public String index(@RequestBody ArenaUpdate arenaUpdate) {
    System.out.println(arenaUpdate);

    PlayerState self = arenaUpdate.arena.state.get(arenaUpdate._links.self.href);
    for (String href : arenaUpdate.arena.state.keySet()) {
      if (!href.equals(arenaUpdate._links.self.href)) {
        PlayerState state = arenaUpdate.arena.state.get(href);
        int dx = state.x - self.x;
        int dy = state.y - self.y;
        if (self.direction.equals("N")) {
          if (dy < 0 && dy >= -RANGE) {
            return "F";
          }
        } else if (self.direction.equals("S")) {
          if (dy > 0 && dy <= RANGE) {
            return "F";
          }
        } else if (self.direction.equals("W")) {
          if (dx < 0 && dx >= -RANGE) {
            return "F";
          }
        } else if (self.direction.equals("E")) {
          if (dx > 0 && dx <= RANGE) {
            return "F";
          }
        }
      }
    }

    String[] commands = new String[]{"F", "R", "L", "T"};
    int i = new Random().nextInt(4);
    return commands[i];
  }

}

