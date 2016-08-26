package com.turkoid.rest.dao;

import com.turkoid.rest.dao.common.DaoResult;
import com.turkoid.rest.dao.common.DatabaseUtils;
import com.turkoid.rest.dao.jooq.practiceone.tables.records.UserRecord;
import com.turkoid.rest.model.User;
import org.jooq.*;
import org.jooq.conf.ParamType;

import javax.ws.rs.PathParam;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.turkoid.rest.dao.jooq.practiceone.Tables.USER;
import static org.jooq.impl.DSL.*;

/**
 * Created by turkoid on 8/21/2016.
 */
public class UserDao {

    public static DaoResult<User> getUsers() throws Exception {
        DSLContext jooq = DatabaseUtils.getJooqDslContext();
        Result<UserRecord> result = jooq.selectFrom(USER).fetch();

        List<User> users = new ArrayList<>();
        for (UserRecord userRecord : result) {
            User user = new User();
            user.setId(userRecord.getId());
            user.setName(userRecord.getName());
            user.setAttending(userRecord.getStatus());
            users.add(user);
        }

        return new DaoResult<>(users, DaoResult.Operation.READ);
    }

    public static DaoResult<User> getUser(int id) throws Exception {
        DSLContext jooq = DatabaseUtils.getJooqDslContext();
        UserRecord userRecord = jooq.selectFrom(USER)
                .where(USER.ID.eq(id))
                .fetchOne();

        User user = null;
        if (userRecord != null) {
            user = new User();
            user.setId(userRecord.getId());
            user.setName(userRecord.getName());
            user.setAttending(userRecord.getStatus());
        }

        return  new DaoResult<>(user, DaoResult.Operation.READ);
    }

    public static DaoResult<User> addUser(User user, boolean updateOnDuplicate) throws Exception {
        if (user != null) {
            DSLContext jooq = DatabaseUtils.getJooqDslContext();


            UserRecord userRecord = jooq.newRecord(USER);
            if (user.getId() != null) userRecord.setId(user.getId());
            userRecord.setName(user.getName());
            if (user.isAttending() != null) userRecord.setStatus(user.isAttending());

            //jooq 3.8 doesn't support chaining 'returning' and 'onduplicatekeyupdate'
            InsertQuery<UserRecord> qry = jooq.insertQuery(USER);
            qry.setRecord(userRecord);
            qry.setReturning(USER.ID);
            if (updateOnDuplicate) {
                qry.onDuplicateKeyUpdate(true);
                Map<Field<?>, ?> map = Arrays.stream(USER.fields())
                        .filter(field -> !USER.ID.equals(field)) //don't update id field again
                        .filter(field -> userRecord.getValue(field) != null) //only map fields with values
                        .collect(Collectors.toMap(Function.identity(), userRecord::getValue)); //field -> value
                qry.addValuesForUpdate(map);
            }
            int result = qry.execute();
            if (result == 1) {
                //if it inserts, the count is 1
                Record record = qry.getReturnedRecord();
                if (record != null) {
                    //so there is a special case where the result will be 1, but it was not inserted and nothing changed
                    user.setId(record.getValue(USER.ID));
                    return new DaoResult<>(user, DaoResult.Operation.CREATED);
                }
            }

            return new DaoResult<>(user, DaoResult.Operation.UPDATED);
        }
        return DaoResult.NULL;
    }

    public static DaoResult<User> addUser(User user) throws Exception {
        return addUser(user, false);
    }

    public static DaoResult<User> updateUser(User user) throws Exception {
        if (user != null) {
            DSLContext jooq = DatabaseUtils.getJooqDslContext();
            UserRecord userRecord = new UserRecord();
            if (user.getId() != null) userRecord.setId(user.getId());
            userRecord.setName(user.getName());
            if (user.isAttending() != null) userRecord.setStatus(user.isAttending());

            int rowsUpdated = jooq.update(USER)
                    .set(userRecord)
                    .execute();

            return new DaoResult<>(user, DaoResult.Operation.UPDATED);
        }
        return DaoResult.NULL;
    }

    public static DaoResult<User> deleteUser(int id) throws Exception {
        DSLContext jooq = DatabaseUtils.getJooqDslContext();
        int rowsDeleted = jooq.delete(USER)
                .where(USER.ID.eq(id))
                .execute();

        return new DaoResult<>(DaoResult.Operation.DELETED, rowsDeleted);
    }

    public static DaoResult<User> deleteUsers() throws Exception {
        DSLContext jooq = DatabaseUtils.getJooqDslContext();
        int rowsDeleted = jooq.delete(USER).execute();
        return new DaoResult<>(DaoResult.Operation.DELETED, rowsDeleted);
    }
}
