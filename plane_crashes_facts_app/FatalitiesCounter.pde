class FatalitiesCounter{
  private int ground =0;
  private int passengers =0;
  private int total = 0;
  
  FatalitiesCounter(String phase,String year,String type ,ArrayList<Accident> accidents){
    for(Accident a : accidents){
      if(a.getType() == type){
        increasePassengers(a.getPassengerDeaths());
        increasePassengers(a.getCrewDeaths());
        increaseGround(a.getGroundDeaths());
      }
    }
  }
  
  public void increaseGround(int g){
  ground+=g;
  total+=g;
  }
  
  public void increasePassengers(int p){
  passengers+=p;
  total+=p;
  }
  
  public int getGround(){
    return ground;
  }

  public int getPassengers(){
    return passengers;
  }
  
  public int getTotal(){
    return total;
  }
  
}