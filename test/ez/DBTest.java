package ez;
import static com.google.common.base.Preconditions.checkState;
import static ox.util.Utils.count;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ez.impl.MySQLDB;

public class DBTest {

  private DB db = new MySQLDB("localhost", "root", "", "ezdb-test", false, 10);

  private Table table;

  @BeforeEach
  public void before() {
    if (db.hasTable("user")) {
      db.deleteTable("user");
    }
    
    table = new Table("user")
        .idColumn()
        .column("firstName", String.class)
        .column("lastName", String.class);
    db.addTable(table);
  }

  @Test
  public void testInsert() {
    int n = 10_000;
    count(1, n).concurrent().forEach(z -> {
      db.insert(table, new Row().with("firstName", n).with("lastName", n));
    });

    checkState(db.select("SELECT * FROM user").size() == n);
  }
  
  // @Test
  // public void streamingTest() {
  // int n = 100;
  // count(1, n).concurrent().forEach(z -> {
  // db.insert(table, new Row().with("firstName", n).with("lastName", n));
  // });
  // }
  

}
