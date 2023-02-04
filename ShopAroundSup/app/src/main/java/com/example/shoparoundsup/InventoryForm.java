package com.example.shoparoundsup;

import static com.example.shoparoundsup.RegVendorDetails.PICK_IMAGE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.shoparoundsup.adapters.AddItemRVAdapter;
import com.example.shoparoundsup.model.InventoryItemModel;
import com.example.shoparoundsup.util.RegPreferenceManager;
import com.example.shoparoundsup.util.RegShopPreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class InventoryForm extends AppCompatActivity {


    Button registerBtn;
    RecyclerView itemsRV;
    EditText itemNameEt, itemPriceEt;
    Button addBtn;
    ImageView itemImage;

    List<InventoryItemModel> list = new ArrayList<>();
    AddItemRVAdapter adapter;

    RegPreferenceManager vendorPref;
    RegShopPreferenceManager shopPref;
    Uri itemImgUri;
    FirebaseFirestore fStore;
    FirebaseUser fUser;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    final StorageReference profileRef = storageReference.child("VendorProfilePic/" + UUID.randomUUID().toString());
    final StorageReference shopRef = storageReference.child("VendorShopProfile/"+ UUID.randomUUID().toString());
    StorageReference itemsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_form);

        registerBtn = findViewById(R.id.registerBtn);
        itemsRV = findViewById(R.id.addedItemsRv);
        itemNameEt = findViewById(R.id.itemNameET);
        itemPriceEt = findViewById(R.id.itemPriceET);
        addBtn = findViewById(R.id.addBtn);
        itemImage = findViewById(R.id.itemImage);

        fStore = FirebaseFirestore.getInstance();
        fUser = FirebaseAuth.getInstance().getCurrentUser();

        itemsRV.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new AddItemRVAdapter(list);
        itemsRV.setAdapter(adapter);

        itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = itemNameEt.getText().toString().trim();
                String price = itemPriceEt.getText().toString().trim();

                InventoryItemModel data = new InventoryItemModel(name, price, itemImgUri);
                list.add(data);
                adapter.notifyItemInserted(list.size()-1);
                itemNameEt.setText("");
                itemPriceEt.setText("");
            }
        });



        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                vendorPref = new RegPreferenceManager(InventoryForm.this);
                shopPref = new RegShopPreferenceManager(InventoryForm.this);

                Uri[] uris = new Uri[2];

                UploadTask uploadTaskProf = profileRef.putFile(Uri.parse(vendorPref.getProfileImage()));
                UploadTask uploadTaskShop = shopRef.putFile(Uri.parse(shopPref.getShopImage()));


                uploadTaskProf.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                uris[0] = uri;
                                uploadTaskShop.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                uris[1] = uri;

                                                Toast.makeText(InventoryForm.this, uris[0].toString(), Toast.LENGTH_LONG).show();
                                                Toast.makeText(InventoryForm.this, uris[1].toString(), Toast.LENGTH_LONG).show();

                                                Map<String, Object> map = new HashMap<>();
                                                map.put("Name", vendorPref.getName());
                                                map.put("Email", vendorPref.getEmail());
                                                map.put("Age", vendorPref.getAge());
                                                map.put("Gender", vendorPref.getGender());
                                                map.put("ShopName", shopPref.getshopName());
                                                map.put("Address", shopPref.getaddress());
                                                map.put("City", shopPref.getcity());
                                                map.put("Pincode", shopPref.getPincode());
                                                map.put("Category", shopPref.getCategory());
                                                map.put("ProfileImage", String.valueOf(uris[0]));
                                                map.put("ShopImage", String.valueOf(uris[1]));
                                                map.put("Inventory", list);

                                                pushData(map);


                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(InventoryForm.this, "Error! Shop Image", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(InventoryForm.this, "Error! Profile Image", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

            }
        });

    }

    void pushData(Map<String, Object> map){
        fStore.collection("Vendors").document(shopPref.getcity()).collection(shopPref.getCategory()).document(fUser.getPhoneNumber()).set(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(InventoryForm.this, "Vendor Succeessfully Registered(Categorized)", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(InventoryForm.this, MainActivity.class);
                        startActivity(i);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(InventoryForm.this, "Can't Register Vendor.(Categorized)", Toast.LENGTH_SHORT).show();
                    }
                });

        fStore.collection("AllVendors").document(fUser.getPhoneNumber()).set(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {



                        Toast.makeText(InventoryForm.this, "Vendor Succeessfully Registered(All)", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(InventoryForm.this, MainActivity.class);
                        startActivity(i);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(InventoryForm.this, "Can't Register Vendor.(All)", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public static final int PICK_IMAGE = 1;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            itemImgUri = data.getData();
            itemImage.setImageURI(itemImgUri);
        }
    }
}