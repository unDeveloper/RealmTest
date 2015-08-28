package app.com.example.android.realmtest.classes;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by hunabsys-mano on 28/07/15.
 */
public class Position extends RealmObject {

    @PrimaryKey
    private int id;

    private String positionName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }
}
