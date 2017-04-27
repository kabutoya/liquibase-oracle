package liquibase.ext.ora.dblink;

import liquibase.exception.ValidationErrors;

public class DBLinkGeneratorUtil {

    public enum OperationType {
        CREATE,ALTER,DROP
    }

    public static String makeCommonParts(DbLinkState statement, OperationType type) {
        StringBuilder sb = new StringBuilder();
        switch (type) {
            case CREATE:
                sb.append("CREATE ");
                break;
            case ALTER:
                sb.append("ALTER ");
                break;
            case DROP:
                sb.append("DROP ");
        }
        sb.append(addIfNotNull(DbLinkState.TYPE_SHARED, statement.isSharedType()))
           .append(addIfNotNull(DbLinkState.TYPE_PUBLIC, statement.isPublicType()))
           .append(" DATABASE LINK ")
           .append(statement.getDblinkName());
        return sb.toString();
    }
    public static String makeConnectParts(DbLinkState statement) {
        StringBuilder sb = new StringBuilder();
        sb.append(" CONNECT TO ")
          .append(statement.getUser())
          .append(" IDENTIFIED BY ")
          .append(statement.getPassword());

        return sb.toString();
    }
    public static String makeAuthParts(DbLinkState statement) {
        StringBuilder sb = new StringBuilder();
        sb.append(addIfNotNull(String.format(" AUTHENTICATED BY %s IDENTIFIED BY %s ", statement.getAuthUser(), statement.getAuthPassword())
                , statement.isSharedType()));
        return sb.toString();
    }

    public static String addIfNotNull(String data, boolean option) {
        if(option && !isEmpty(data)) {
            return data;
        }
        return "";
    }

    public static boolean isEmpty(String data) {
        return data == null || data.length() <= 0;
    }

    public static ValidationErrors makeValidate(DbLinkState statement, String additionalMsg) {
        ValidationErrors validationErrors = new ValidationErrors();
        validationErrors.checkRequiredField("dblinkName", statement.getDblinkName());
        validationErrors.checkRequiredField("user", statement.getUser());
        validationErrors.checkRequiredField("password", statement.getPassword());
        if(statement.isSharedType()) {
            validationErrors.checkRequiredField("authUser", statement.getAuthUser());
            validationErrors.checkRequiredField("authPassword", statement.getAuthPassword());
        }
        String msg = isEmpty(additionalMsg) ? "type must be \"SHARED\" or \"PUBLIC\" or empty" : additionalMsg;
        if(!isEmpty(statement.getType()) && !statement.isSharedType() && !statement.isPublicType()) {
            validationErrors.addError(msg);
        }
        return validationErrors;
    }

}
