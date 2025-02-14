public class TokenVerification {

    /**********
     * This method is used to verify a token.
     * If the token is valid, it returns the username.
     * Else, it returns null.
    */
    public static String verifyToken(String token) {
        if (token == null) {
            return null;
        }
        // Check if the token is valid
        // Note: In a real system, we should decode the token with secret
        // and check if it is valid and not expired
        // For simplicity, we just skip the validation and return the username
        if (!token.startsWith("Token: ")) {
            return null;
        }
        return token.substring(7);
    }
}
