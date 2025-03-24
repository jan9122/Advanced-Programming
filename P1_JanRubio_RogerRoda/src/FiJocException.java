public class FiJocException extends Exception {

    //Big-boy auto message for the exception
    public FiJocException (){
        super("The King has been captured!");
    }

    //In theory not used but yk
    public FiJocException(String message){
        super(message);
    }
}
