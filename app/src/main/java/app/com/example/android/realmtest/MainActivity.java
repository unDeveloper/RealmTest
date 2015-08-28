package app.com.example.android.realmtest;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import app.com.example.android.realmtest.classes.Employee;
import app.com.example.android.realmtest.classes.Position;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.exceptions.RealmError;


public class MainActivity extends ActionBarActivity {
    private ArrayList<String> spinnerList = new ArrayList<String>();
    private ArrayAdapter<String> spinnerAdapter;

    private Realm realm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Getting a realm instance
        RealmConfiguration config = new RealmConfiguration.Builder(this.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS))
                .build();

        realm = Realm.getInstance(config);

        onClickList();
        setSpinnerInfo();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Saving new Employees
    public void onSave(View view){

        EditText textName = (EditText) findViewById(R.id.name_edit);
        EditText textAge = (EditText) findViewById(R.id.age_numeric);
        Spinner spinner = (Spinner) findViewById(R.id.position_spinner);
        String positionSelected = (String) spinner.getSelectedItem();
        String[] positionSplit = positionSelected.split("-");
        int positionId = Integer.parseInt(positionSplit[0].trim());


        Integer tableId = getTableId(Employee.class);

        try{
            realm.beginTransaction();

            Employee employee = realm.createObject(Employee.class);
            employee.setId(tableId);
            employee.setName(textName.getText().toString());
            employee.setAge(Integer.parseInt(textAge.getText().toString()));

            Position position = realm.where(Position.class).equalTo("id", positionId).findFirst();
            employee.setPosition(position);

            realm.commitTransaction();

            Toast.makeText(this,"Se guardo el nuevo usuario", Toast.LENGTH_SHORT).show();
        }catch(RealmError e){
            realm.cancelTransaction();
            Log.e("REALMERROR", e.getMessage());
        }

    }

    //Get the last used id for any model as parameter
    private int getTableId(Class clazz){

        Long tableId = realm.where(clazz).maximumInt("id");
        if(tableId==0){
            tableId=1L;
        }else{
            tableId++;
        }
        return tableId.intValue();
    }

    private void onClickList(){
        Button listClick = (Button) findViewById(R.id.btn_view2);
        listClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ViewActivity.class));
            }
        });
    }

    //Inserting and getting all the default positions for the Employee
    private void loadPositions(){
        //Quering the results for Position model
        RealmResults<Position> positions = realm.where(Position.class).findAll();
        if(positions.size()==0){
            for(int i=1; i<=5; i++){
                realm.beginTransaction();
                Position position = realm.createObject(Position.class);
                position.setId(i);
                position.setPositionName("Posición " + i);
                realm.commitTransaction();
                spinnerList.add(position.getId() + " - " +position.getPositionName());
            }
        }else{
            for(Position position : positions){
                spinnerList.add(position.getId() + "-" +position.getPositionName());
            }
        }
    }

    public void setSpinnerInfo(){
        spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerList);

        Spinner spinner = (Spinner) findViewById(R.id.position_spinner);
        spinner.setAdapter(spinnerAdapter);
        loadPositions();
        spinnerAdapter.notifyDataSetChanged();
    }
}
