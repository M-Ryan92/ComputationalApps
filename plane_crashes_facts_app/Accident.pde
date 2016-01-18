class Accident implements Comparable<Accident>{
  private int totalDeaths;
  private int crewDeaths;
  private int passengerDeaths;
  private int groundDeaths;
  private String notes;
  
  private String type;
  private String incident;
  private String aircraft;  
  private String phase;
  private Calendar date;
  
  //@override
  public int compareTo(Accident compareAccident) {
      Calendar compDate = ((Accident)compareAccident).getDate();
      return this.date.compareTo(compDate);
  }

  Accident(TableRow row){
    parseRow(row);
  }  
  
  private void parseRow(TableRow row){
    totalDeaths = row.getInt(0);
    crewDeaths = row.getInt(1);
    passengerDeaths = row.getInt(2);
    groundDeaths = row.getInt(3);
    
    notes = row.getString(4).trim();
    type = row.getString(5).trim();
    incident = row.getString(6).trim();
    aircraft = row.getString(7).trim();
    phase = row.getString(9).trim();
    
    if(phase.contains(" ")){
      phase = phase.split(" ")[0];
    }
    
    date = Util.parseStringToCal(row.getString(12),"MM/dd/YYYY");
  }  
  
  public int getPassengerDeaths(){
    return passengerDeaths;
  }
  
  public int getCrewDeaths(){
    return crewDeaths;
  }  
  
  public int getGroundDeaths(){
    return groundDeaths;
  }    
  
  
  public String getType(){
    return type;
  }
  public String getPhase(){
    return phase;
  }
  public Calendar getDate(){
    return date;
  }
  public String getDateAsString(){
    return Util.parseCalToString(date,"MM/dd/YYYY");
  }  
  
}