class Rows{
  private int rowHeight;
  private int[] yoffset;
  private int spacingX,spacingY;

  Rows(int rows, int spacingX, int spacingY, int[] yoffset){
    this.spacingX = spacingX;
    this.spacingY = spacingY;
    this.yoffset = yoffset;
    rowHeight = height/rows - spacingY - (yoffset[0]/ rows) - (yoffset[1]/ rows);
  };

  public void drawRow(int row){
    drawRow(row, 0);
  }
  
  public void drawRow(int row, int offset){
    int b[] = getRowBoundary(row);
    rect(b[0], b[1] + offset , b[2], b[3] - offset);
  }
  
  public int[] getRowBoundary(int row){
    int x = spacingX;
    int y = yoffset[0]+ (row * rowHeight) + (spacingY * row);
    return new int[]{x,y,width - (spacingX*2), rowHeight};
  }

}