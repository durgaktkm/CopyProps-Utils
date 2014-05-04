import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Ramprasad on 5/3/14.
 */
public class EitherTest {

    static final String USER_NAME = "username";
    static final String ROOT = "root";

    static class AuthLevel {
    }

    static final AuthLevel GUEST = new AuthLevel();
    static final AuthLevel USER = new AuthLevel();
    static final AuthLevel SUPERUSER = new AuthLevel();

    public static class NoSuchUserException extends Exception {
    }

    public static class UnknownSettingException extends Exception {
    }

    public static class SettingGetter {

        private String userName;

        public SettingGetter(final String name) {
            userName = name;
        }

        Either<String> get_setting(final String settingName) {
            // Simple way to test out both having and not having a username
            if (settingName.equals(USER_NAME)) {
                if (userName != null) { return new Either<>(userName); }
                else { return new Either<>(new NoSuchUserException()); }
            }
            else return new Either<>(new UnknownSettingException());
        }
    }

    public static class Authorizer {
        public AuthLevel getAuthorizationLevelForUser(final String user) {
            if ( ROOT.equals(user) ) {
                return SUPERUSER;
            }
            else {
                return USER;
            }
        }
    }

    public static class Decider {
        public Boolean canDoAnything( final AuthLevel l ) {
            return SUPERUSER.equals(l);
        }
    }

    @Test
    public void authLevelTests() {
        {
            final SettingGetter sg = new SettingGetter("joe");
            final Authorizer authorizer = new Authorizer();
            final Either<AuthLevel> authLevel = getAuthorizationLevel(sg,authorizer);
            assertEquals(USER,authLevel.right());
        }
        {
            final SettingGetter sg = new SettingGetter(null);
            final Authorizer authorizer = new Authorizer();
            final Either<AuthLevel> authLevel = getAuthorizationLevel(sg,authorizer);
            assertEquals(null,authLevel.right());
            assertEquals(NoSuchUserException.class,authLevel.left().getClass());
        }
        {
            final SettingGetter sg = new SettingGetter(null);
            final Authorizer authorizer = new Authorizer();
            final Either<AuthLevel> authLevel = getAuthorizationLevel(sg,authorizer);
            final AuthLevel level = authLevel.getOrElse(GUEST);
            assertEquals(GUEST,level);
        }
        {
            final SettingGetter sg = new SettingGetter(ROOT);
            final Authorizer authorizer = new Authorizer();
            final Decider decider = new Decider();
            final Either<Boolean> isAllPowerful = sg.get_setting(USER_NAME).rightMap(authorizer::getAuthorizationLevelForUser).rightMap(decider::canDoAnything);
            assertTrue(isAllPowerful.hasValue());
            assertEquals(true, isAllPowerful.right() );
        }
    }

    public Either<AuthLevel> getAuthorizationLevel(final SettingGetter sg, final Authorizer authorizer ) {
        Either<String> possibleUser = sg.get_setting(USER_NAME);
        return possibleUser.rightMap(authorizer::getAuthorizationLevelForUser);
    }
}