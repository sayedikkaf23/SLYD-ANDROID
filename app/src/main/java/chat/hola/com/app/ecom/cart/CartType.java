package chat.hola.com.app.ecom.cart;

/**
 * Purpose - Use while initializing {@link CartAdapter}
 * in order to recognize where click event happened
 * specially useful in pharmacy store
 */
public enum CartType {
  ECOM_CART,
  PHARMACY_PRESCRIPTION_CART,
  PHARMACY_WITHOUT_PRESCRIPTION_CART
}
