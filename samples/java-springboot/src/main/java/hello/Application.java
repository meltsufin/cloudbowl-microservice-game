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
  static int RANGE = 3;

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
    String result = null;

    if (iAmATarget(arenaUpdate)) {
      result = getRandomAction(new String[]{"F", "R"});
      System.out.println("I am in range. Random move: " + result);
    } else if (isTargetInRange(arenaUpdate)) {
      result = "T";
      System.out.println("Target is in range. Throwing!");
    } else {
      result = getMoveTowardsCenter(arenaUpdate);
      System.out.println("Target is not range. Move towards center: " + result);

    }

    return result;
  }

  private boolean iAmATarget(ArenaUpdate arenaUpdate) {
    PlayerState self = arenaUpdate.arena.state.get(arenaUpdate._links.self.href);
    for (String href : arenaUpdate.arena.state.keySet()) {
      if (!href.equals(arenaUpdate._links.self.href)) {
        PlayerState state = arenaUpdate.arena.state.get(href);
        int dx = state.x - self.x;
        int dy = state.y - self.y;
        // System.out.printf("dx = %s dy = %s; facing = %s\n", dx, dy, self.direction);
        if (state.direction.equals("S")) {
          if (dx == 0 && dy < 0 && dy >= -RANGE) {
            return true;
          }
        } else if (state.direction.equals("N")) {
          if (dx == 0 && dy > 0 && dy <= RANGE) {
            return true;
          }
        } else if (state.direction.equals("E")) {
          if (dy == 0 && dx < 0 && dx >= -RANGE) {
            return true;
          }
        } else if (state.direction.equals("W")) {
          if (dy == 0 && dx > 0 && dx <= RANGE) {
            return true;
          }
        }
      }
    }
    return false;
  }

  private boolean isTargetInRange(ArenaUpdate arenaUpdate) {
    PlayerState self = arenaUpdate.arena.state.get(arenaUpdate._links.self.href);
    for (String href : arenaUpdate.arena.state.keySet()) {
      if (!href.equals(arenaUpdate._links.self.href)) {
        PlayerState state = arenaUpdate.arena.state.get(href);
        int dx = state.x - self.x;
        int dy = state.y - self.y;
        // System.out.printf("dx = %s dy = %s; facing = %s\n", dx, dy, self.direction);
        if (self.direction.equals("N")) {
          if (dx == 0 && dy < 0 && dy >= -RANGE) {
            return true;
          }
        } else if (self.direction.equals("S")) {
          if (dx == 0 && dy > 0 && dy <= RANGE) {
            return true;
          }
        } else if (self.direction.equals("W")) {
          if (dy == 0 && dx < 0 && dx >= -RANGE) {
            return true;
          }
        } else if (self.direction.equals("E")) {
          if (dy == 0 && dx > 0 && dx <= RANGE) {
            return true;
          }
        }
      }
    }
    return false;
  }

  private String getRandomAction(String[] commands) {
    String result;
    int i = new Random().nextInt(commands.length);
    result = commands[i];
    return result;
  }

  private String getMoveTowardsCenter(ArenaUpdate arenaUpdate) {
    PlayerState self = arenaUpdate.arena.state.get(arenaUpdate._links.self.href);
    int w = arenaUpdate.arena.dims.get(0);
    int h = arenaUpdate.arena.dims.get(1);
    int dx = self.x - w / 2;
    int dy = self.y - h / 2;
    if (Math.abs(dx) > Math.abs(dy)) {
      if (dx < 0) {
        if (self.direction.equals("E")) {
          return "F";
        } else {
          return "R";
        }
      } else {
        if (self.direction.equals("W")) {
          return "F";
        } else {
          return "R";
        }
      }
    } else {
      if (dy < 0) {
        if (self.direction.equals("S")) {
          return "F";
        } else {
          return "R";
        }
      } else {
        if (self.direction.equals("N")) {
          return "F";
        } else {
          return "R";
        }
      }
    }
  }
}

