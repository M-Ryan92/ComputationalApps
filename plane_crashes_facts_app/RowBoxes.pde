class RowBoxes{
  private String title;
  
  private int spacing = 5;
  private int headerHeight = 25;
  private int totalUnits = 0;
  private int columnOffsetX;
  private int columnOffsetY = 10;
  
  private int[] boundaryOffsets;
  
  private HashMap<String,Integer> keySizes = new HashMap();
  private HashMap<String, Box> boxes = new HashMap();
  private HashMap<String,Integer> colors;
  HashMap<String,ArrayList<Accident>> values;
  private boolean isAsc = true;
  private boolean hasbutton = true;
  
  private Box btnBox = null;
  private Box hoverText = null;

  
  RowBoxes(String title, int[] offsets, Object[] types, HashMap<String,ArrayList<Accident>> values){
    this.title = title;
    this.boundaryOffsets = offsets;
    this.values = values;
    
    boundaryOffsets[1] += (2 * spacing) +5;
    boundaryOffsets[2] -= ((2 * spacing) +5)+  (2* spacing);
    boundaryOffsets[3] -= ((2 * spacing) +5)*2;
    boundaryOffsets[0] += spacing  + (2 * spacing);
    
    for(Object s : types){
      keySizes.put(s.toString(), values.get(s).size());
    }
    if(hasbutton) {
    keySizes = Util.sortByValueAsc(keySizes);
    }
    
    for(Entry<String,Integer> entry : keySizes.entrySet()){
      totalUnits += entry.getValue();
    }    
  }

 public boolean update(){
   boolean u = false;
   if(btnBox != null){
     if(btnBox.isClicked()){
       u = true;
       isAsc = isAsc ? false : true;
       boxes = new HashMap();
       if(isAsc && hasbutton){
          btnBox.setName("size >");
          keySizes = Util.sortByValueAsc(keySizes);
       } else if(hasbutton){
          btnBox.setName("< size");
          keySizes = Util.sortByValueDesc(keySizes);
       }
     }
     btnBox.update();
   }
   if(boxes.size() > 0){
      boolean hoverflag= false;
      for(Entry e : boxes.entrySet()){
        Box b = (Box)e.getValue();
        Box bu = null;
        bu = b.update();
        if(bu != null){
          hoverflag= true;
          hoverText.setName(bu.getFullName());
        }
      }
      if(!hoverflag){
        hoverText.setName(" ");
      }
      hoverText.update();      
   }
   return u;
 } 
 
  public void display(){
    drawHeader();
    drawColumn();
  }
  
  private int pushdown = 0;
  private int makesmaller = 0;
  private void drawHeader(int pushDown, int makesmaller){
    this.pushdown = pushDown;
    this.makesmaller = makesmaller;
    drawHeader();
  }
  private void drawHeader(){
    fill(textc);
    textFont(xScale);
    textSize(18);
    text(title, boundaryOffsets[0], boundaryOffsets[1]-5);
    textFont(openSans);
    int pos = (int)textWidth(title) + 10 + boundaryOffsets[0];
    textSize(13);
    int w = (int)textWidth("< size >") + (spacing * 2);
    columnOffsetX = pos + w - (spacing * 2);    
    
    if(btnBox == null && hasbutton){
      float[] rect = new float[]{pos, boundaryOffsets[1]+ columnOffsetY, w, headerHeight};
      btnBox = new Box("size >", rect, 0,btnc, btnch);      
    } else if(hasbutton){
      btnBox.update();
    }
    
    if(hoverText == null){
      int y =  boundaryOffsets[1] + columnOffsetY + headerHeight + 5 + pushdown;
      int h = boundaryOffsets[3] - columnOffsetY - headerHeight - 5 - pushdown;
      
      float[] rect = new float[]{boundaryOffsets[0], y, columnOffsetX - (spacing * 3) - makesmaller, h};
      hoverText = new Box(" ", rect, 0, bg, bg);
      hoverText.setTextColor(textc);
    }
    
  }
   private void drawColumn(){
     if(boxes.size() == 0){
       float unitOffset = 0;
       int maxWidth = (boundaryOffsets[2]) - (boundaryOffsets[0]+ columnOffsetX) + (2*spacing);
       HashMap<String,Integer> tks = keySizes;
     
       if(isAsc){
         tks = Util.sortByValueAsc(keySizes);
       } else {
         tks = Util.sortByValueAsc(keySizes);
         unitOffset = maxWidth + (2* keySizes.size());
       }
       for(Entry<String,Integer> e : tks.entrySet()){
         float size = map(e.getValue(), 0, totalUnits, -2, maxWidth);
         if(size < 13){
          maxWidth -= (13 - size);
          size = 13;
         }
         int c = color(198, 194, 194, 100);
         if(colors != null && colors.containsKey(e.getKey())){
            c = colors.get(e.getKey());
          }
          if(!isAsc){
            unitOffset -= size;
          }
          
          float[] bounds = new float[]{
            boundaryOffsets[0]+ columnOffsetX + unitOffset,
            boundaryOffsets[1] + columnOffsetY,
            size,
            headerHeight
          };
          
          Box b = new Box(e.getKey(), bounds, e.getValue(), c);
          boxes.put(e.getKey(),b);
          if(isAsc){
            unitOffset += size + 2;
          } else {
            unitOffset -= 2;
          }
       }
       
     }
   }
   
   private boolean wait = false;
   private Box my, yours;
   private int con1 = 0;
   private int con2 = 0;
   private ArrayList<Box> myBoxes, theirBoxes;
   private float tw = 0;

   private boolean reset = false;
   public void resetConnections(boolean force){
     ArrayList<Box> all = new ArrayList();
     reset = true;
     if(myBoxes!=null || theirBoxes!=null){
       all.addAll(myBoxes);
       if(force){
         all.addAll(theirBoxes);
       }
       for(Box b :  all) {
         b.resetOffset();
         b.step=0;
       };
       con1=0;
       con2=0;
       wait=false;
     }
   }

    boolean prepareConnector(HashMap<String,Box> connectBoxes){
       wait = true;
       
       myBoxes = new ArrayList(boxes.values());
       theirBoxes = new ArrayList(connectBoxes.values());         
       Collections.sort(myBoxes);
       Collections.sort(theirBoxes);
       
       if(my != null && yours != null && tw >0 && !reset){
           my.increaseOffset(tw);
           yours.increaseOffset(tw);
       } else {
         reset = false;
       }
       if(myBoxes.size() == 0 || theirBoxes.size() == 0) return true;
       my = myBoxes.get(con1);
       yours = theirBoxes.get(con2);
       
       if(my.getOffset() != 0 && con2==0){
         my.resetOffset();
       }
       
       if(con2 + 1 < theirBoxes.size()){
         con2++;
       } else if(con1 + 1 < myBoxes.size()){
         con2 = 0;
         con1++;
       } else{
         return true;
       }
       return false;
    }
   public boolean connectAll(HashMap<String,HashMap<String,ArrayList<Accident>>> results, HashMap<String,Box> connectBoxes){
     if(!wait){
       boolean prep = prepareConnector(connectBoxes);
       if(prep) return prep; //<>//
     } else {
       HashMap<String,ArrayList<Accident>> r = results.get(my.getName()); //<>//
       ArrayList<Accident> typeRes = r.get(yours.getName());
       if(typeRes !=null){
         float v = r.get(yours.getName()).size();
         tw = map(v, 1 , yours.getValue(), 0 , yours.getWidth());
         if(tw <= 3) tw = 3;
         wait = !connect(my, yours, tw);
       } else {
         tw=0;
         wait = false;
       }
     }
     return false;
   }
 
   public boolean connect(Box a, Box b, float size){
     return a.connect(b, size, a.getColor());
   }
 
   private void setHasButton(boolean check){
    hasbutton=check;
   }
 
 //getters and setters
  public void setDataColors(HashMap<String,Integer> c){
    colors = c;
  }
  
  public HashMap<String, Box> getColBoxes(){
    return boxes;
  }
  
  public int getColumnOffsetX(){
    return columnOffsetX;
  }
  
}