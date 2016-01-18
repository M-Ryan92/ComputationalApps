static class Util{
  static Calendar parseStringToCal(String date, String format) {
    SimpleDateFormat formatter = new SimpleDateFormat(format);
    Calendar cal = Calendar.getInstance();
    try {
      cal.setTime(formatter.parse(date));
      return cal;
    } catch(ParseException ex){
      print(ex);
      return null;
    }
  }
  
  static String parseCalToString(Calendar date, String format) {
    SimpleDateFormat formatter = new SimpleDateFormat(format);
    return formatter.format(date);
  }
  
  //group by type
  static HashMap<String,ArrayList<Accident>> groupAccidentsByType(ArrayList<Accident> accidents) {
    HashMap<String,ArrayList<Accident>> accidentsByType = new HashMap();        
    for(Accident accident : accidents){
      String type = accident.getType();
      if(accidentsByType.containsKey(type)){
        accidentsByType.get(type).add(accident);
      } else {
        accidentsByType.put(type, new ArrayList());
        accidentsByType.get(type).add(accident);
      }
    }
    return accidentsByType;
  }

  //group by phase
  static HashMap<String,ArrayList<Accident>> groupAccidentsByPhase(ArrayList<Accident> accidents) {
    HashMap<String,ArrayList<Accident>> accidentsByPhase = new HashMap();        
    for(Accident accident : accidents){
      String phase = accident.getPhase();
      if(accidentsByPhase.containsKey(phase)){
        accidentsByPhase.get(phase).add(accident);
      } else {
        accidentsByPhase.put(phase, new ArrayList());
        accidentsByPhase.get(phase).add(accident);
      }
    }
    return accidentsByPhase;
  }  

  static HashMap<String,HashMap<String,ArrayList<Accident>>> groupAccidentsByTypeAndPhase(ArrayList<Accident> accidents) {
    HashMap<String,HashMap<String,ArrayList<Accident>>> accidentsbyTypeAndPhase = new HashMap();
    for(Accident accident : accidents){
      String type = accident.getType();
      String phase = accident.getPhase();

      if(accidentsbyTypeAndPhase.containsKey(type)){
        HashMap<String,ArrayList<Accident>> groupedByType = accidentsbyTypeAndPhase.get(type);
        if(groupedByType.containsKey(phase)){
          groupedByType.get(phase).add(accident);
        } else {
          groupedByType.put(phase, new ArrayList());
          groupedByType.get(phase).add(accident);
        }
      } else {
        accidentsbyTypeAndPhase.put(type, new HashMap());
        accidentsbyTypeAndPhase.get(type).put(phase, new ArrayList());
        accidentsbyTypeAndPhase.get(type).get(phase).add(accident);
      }
    }
    return accidentsbyTypeAndPhase;
  }
  static LinkedHashMap<String, ArrayList<Accident>> groupAccidentsByYear(ArrayList<Accident> accidents){
    LinkedHashMap<String,ArrayList<Accident>> accidentsbyYear = new LinkedHashMap();
    for(Accident a : accidents){
      Calendar c = a.getDate();
      String y = String.valueOf(c.get(Calendar.YEAR));
      if(accidentsbyYear.containsKey(y)){
        accidentsbyYear.get(y).add(a);
      } else {
        ArrayList<Accident> l = new ArrayList();
        l.add(a);
        accidentsbyYear.put(y,l);
      }
      
    }
    return accidentsbyYear;
  }

  static HashMap<String,HashMap<String,ArrayList<Accident>>> groupAccidentsByPhaseAndYear(ArrayList<Accident> accidents) {
    HashMap<String,HashMap<String,ArrayList<Accident>>> accidentsbyPhaseAndYear = new HashMap();
    for(Accident accident : accidents){
      String year = String.valueOf(accident.getDate().get(Calendar.YEAR));
      String phase = accident.getPhase();

      if(accidentsbyPhaseAndYear.containsKey(phase)){
        HashMap<String,ArrayList<Accident>> groupedByPhase = accidentsbyPhaseAndYear.get(phase);
        if(groupedByPhase.containsKey(year)){
          groupedByPhase.get(year).add(accident);
        } else {
          groupedByPhase.put(year, new ArrayList());
          groupedByPhase.get(year).add(accident);
        }
      } else {
        accidentsbyPhaseAndYear.put(phase, new HashMap());
        accidentsbyPhaseAndYear.get(phase).put(year, new ArrayList());
        accidentsbyPhaseAndYear.get(phase).get(year).add(accident);
      }
    }
    return accidentsbyPhaseAndYear;
  }

  public static HashMap sortByValueAsc(HashMap unsortMap) {
  List list = new LinkedList(unsortMap.entrySet());
 
  Collections.sort(list, new Comparator() {
    public int compare(Object o1, Object o2) {
      return ((Comparable) ((Map.Entry) (o1)).getValue())
            .compareTo(((Map.Entry) (o2)).getValue());
    }
  });
 
  HashMap sortedMap = new LinkedHashMap();
  for(Object o : list){
    Map.Entry e = (Map.Entry)o;
    sortedMap.put(e.getKey(), e.getValue());
  }

  return sortedMap;
  }
  
  public static HashMap sortByValueDesc(HashMap unsortMap) {
  List list = new LinkedList(unsortMap.entrySet());
 
  Collections.sort(list, new Comparator() {
    public int compare(Object o1, Object o2) {
      return ((Comparable) ((Map.Entry) (o2)).getValue())
            .compareTo(((Map.Entry) (o1)).getValue());
    }
  });
 
  HashMap sortedMap = new LinkedHashMap();
  for(Object o : list){
    Map.Entry e = (Map.Entry)o;
    sortedMap.put(e.getKey(), e.getValue());
  }

  return sortedMap;
  }
   
}