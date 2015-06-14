
/**
 * Interface GameProtokol.
 */
public interface GameProtokol {

   /** Post do czatu. */
   public static final String POST_COMMAND = "POST:";
   
   /** Lista nickow. */
   public static final String NICKLIST_COMMAND = "LIST:";
   
   /** Komenda logowania. */
   public static final String LOGIN_COMMAND = "LOGIN:";
   
   /** Komenda wylogowania. */
   public static final String LOGOUT_COMMAND = "LOGOUT:";
   
   /** Komenda z nickiem. */
   public static final String NICK_COMMAND = "NICK:";
   
   /** Zgloszenie gotowosci. */
   public static final String READY_COMMAND = "READY:";
   
   /** Udzielenie odpowiedzi. */
   public static final String ANSWER_COMMAND = "ANSWER:";
   
   /** Tresc pytania. */
   public static final String QUESTION_COMMAND = "QUESTION:";
   
   /** Wariant odpowiedzi A. */
   public static final String VAR_A_COMMAND = "VARA:";
   
   /** Wariant odpowiedzi B. */
   public static final String VAR_B_COMMAND = "VARB:";
   
   /** Wariant odpowiedzi C. */
   public static final String VAR_C_COMMAND = "VARC:";
   
   /** Wariant odpowiedzi D. */
   public static final String VAR_D_COMMAND = "VARD:";
   

   public static final String NEXT_COMMAND = "NEXT:";
   public static final String TRUE_COMMAND = "TRUE:";


}