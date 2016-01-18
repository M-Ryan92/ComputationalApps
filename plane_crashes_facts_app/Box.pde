class Point{
  float x,y;
  Point(float x, float y){
    this.x = x;
    this.y = y;
  }
}
class Box implements Comparable<Box>{
  private float x,y,w,h,value;
  float step = 0.00f;
  
  private String name;
  
  private int col;
  private int hover = 0;
  private int offset = 0;
  
  private Point p;
  
  //@override
  public int compareTo(Box compareBox) {
      float compf = compareBox.getWidth();
      return Float.valueOf(compf).compareTo(w);
  }  
  
  private void base(String name, float[] bounds, int col){
    this.name = name;
    this.x = bounds[0];
    this.y = bounds[1];
    this.w = bounds[2];
    this.h = bounds[3];
    this.col = col;
    p = new Point(x,y+h+1);  
  }
  
  Box(String name, float[] bounds, float value, int col, int hover){
    base(name, bounds, col);
    this.value = value;
    this.hover = hover;
    update();    
  }
  
  Box(String name, float[] bounds, float value, int col){
    base(name, bounds, col);
    this.value = value;
    update();
  }
 
 private int txtcolor = 0;
 public void setTextColor(int col){
   txtcolor = col;
 }
  public Box update(){
    if(!hover()){
      fill(col);
      drawBox();
    }else {
      if(hover == 0){
      fill(col);
      } else {
      fill(hover);
      }
      drawBox();
      return this;
    }
    return null;
  }  
  
  private void drawBox(){
    rect(x,y,w,h);
    textSize(14);
    fill(txtcolor);
    textAlign(CENTER,CENTER);
    text(name, x,y,w,h);
    textAlign(LEFT,CENTER);
  }
  
  public boolean connect(Box b, float s, int col){
    fill(col);
    return animateCurve(b,s);
  }
  
  private boolean animateCurve(Box b, float s){
   if(step >= 1.0f){
     step = 0.00f;
     return true;
   }
   int ss=0;
   float sstep= step;
    
   while(ss < 10){
     if(sstep > 1.0f) break;
     float xp = bezierPoint(p.x+ offset, p.x+offset, b.p.x+ b.getOffset() ,  b.p.x+ b.getOffset() , sstep);
     float yp = bezierPoint(p.y , b.p.y - h  , p.y + (2*h), b.p.y - h , sstep);
     rect(xp,yp, s, sqrt(s));
     sstep+= 0.001f;
     ss++;
   }
   if(keyPressed == true){  
     keyPressed = false;
     while(true){
       float xp = bezierPoint(p.x+ offset, p.x+offset, b.p.x+ b.getOffset() ,  b.p.x+ b.getOffset() , step);
       float yp = bezierPoint(p.y  , b.p.y - h  , p.y + (2*h) , b.p.y - (1*h), step);
       rect(xp,yp, s, sqrt(s));
       if(step > 1.0f)break;
       step+=0.001f;
     }
   }
   step += 0.01f;
   update();
   b.update();
    return false;
  }

  public boolean isClicked(){
    boolean mp = false;
    if(hover()){
      if(mousePressed == true){
        mousePressed = false;
        mp = true;
      }
    }
    return mp;
  }  
  
  private boolean hover() {
    if (mouseX >= x && mouseX <= x+w && 
        mouseY >= y && mouseY <= y+h) {
          return true;
    } else {
      return false;
    }
  }  

  public void resetOffset(){
    offset=0;
  }
  
  public void increaseOffset(float o){
    offset+=o;
  }

  //setter and getters down no need to look here
  public String getName(){
    return name;
  }
  
  public void setName(String name){
    this.name = name;
  }

  public int getColor(){
    return col;
  }
  
  public float getValue(){
    return value;
  }
  
  public float getWidth(){
    return w;
  }

  public float getHeigth(){
    return h;
  }
  
  public float getX(){
    return x;
  }
  
  public float getY(){
    return y;
  }
  
  public int getOffset(){
    return offset;
  }
  
  public String getFullName(){
    HashMap<String, String> abbreviations = AbbreviationDictionary.getAbreviationDic();
    if(abbreviations.containsKey(name)){
      return abbreviations.get(name);
    } else {
      return name;
    }
  }
}