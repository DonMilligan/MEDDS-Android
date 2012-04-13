package medds.carecenter.library;

public class TableData {

    //private variables
    int _id;
    String _notes;
    byte[] _photo;
 
    // Empty constructor
    public TableData(){
 
    }
    // constructor
    public TableData(int id, String notes, byte[] _photo){
        this._id = id;
        this._notes = notes;
        this._photo = _photo;
    }
 
    // constructor
    public TableData(String notes, byte[] _photo){
        this._notes = notes;
        this._photo = _photo;
    }
    // getting ID
    public int getID(){
        return this._id;
    }
 
    // setting id
    public void setID(int id){
        this._id = id;
    }
 
    // getting notes
    public String getNotes(){
        return this._notes;
    }
 
    // setting notes
    public void setNotes(String notes){
        this._notes = notes;
    }
 
    // getting phone number
    public byte[] getPhoto(){
        return this._photo;
    }
 
    // setting phone number
    public void setPhoto(byte[] photo){
        this._photo = photo;
    }
}
