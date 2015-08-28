package app.com.example.android.realmtest;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;

import app.com.example.android.realmtest.classes.Employee;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;


public class ViewActivity extends ActionBarActivity {

    Realm realm;
    ArrayList<String> listItems = new ArrayList<String>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_activity);
        RealmConfiguration config = new RealmConfiguration.Builder(this.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS))
                .build();
        realm = Realm.getInstance(config);
        onClickCapture();

        adapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
        ListView list = (ListView) findViewById(R.id.listView);
        list.setAdapter(adapter);
        getDataFromRealm();
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_activity, menu);
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

    public void onClickCapture(){
        Button clickCapture=(Button) findViewById(R.id.btn_view1);
        clickCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewActivity.this, MainActivity.class));
            }
        });
    }

    private void getDataFromRealm(){
        RealmQuery<Employee> query = realm.where(Employee.class);

        RealmResults<Employee> employees = query.findAll();

        for (Employee employee : employees){
            String employeeString = employee.getId() + " - " + employee.getName() + " - " + employee.getPosition().getPositionName();
            listItems.add("Empleado: " + employeeString);
        }
    }
}
