package pl.forexat.nbpscraper;

public class AccountConfiguration
{
  private String username;
  private String password;
  private String authorizationTab;
  private String purchaseTab;
  
  public String getUsername()
  {
    return this.username;
  }
  
  public void setUsername(String username)
  {
    this.username = username;
  }
  
  public String getPassword()
  {
    return this.password;
  }
  
  public void setPassword(String password)
  {
    this.password = password;
  }
  
  public String getAuthorizationTab()
  {
    return this.authorizationTab;
  }
  
  public void setAuthorizationTab(String authorizationTab)
  {
    this.authorizationTab = authorizationTab;
  }
  
  public String getPurchaseTab()
  {
    return this.purchaseTab;
  }
  
  public void setPurchaseTab(String purchaseTab)
  {
    this.purchaseTab = purchaseTab;
  }
  
  public String toString()
  {
    return "Username: " + this.username + " password: " + this.password;
  }
}
