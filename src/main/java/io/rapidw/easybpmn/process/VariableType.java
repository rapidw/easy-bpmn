package io.rapidw.easybpmn.process;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.val;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;
import org.skyscreamer.jsonassert.JSONCompare;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class VariableType implements UserType<Variable> {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public int getSqlType() {
        return Types.VARCHAR;
    }

    @Override
    public Class<Variable> returnedClass() {
        return Variable.class;
    }

    @SneakyThrows
    @Override
    public boolean equals(Variable x, Variable y) {
        return JSONCompare.compareJSON(objectMapper.writeValueAsString(x), objectMapper.writeValueAsString(y), JSONCompareMode.LENIENT).passed();
    }

    @Override
    public int hashCode(Variable x) {
        return x.hashCode();
    }

    @Override
    @SneakyThrows
    public Variable nullSafeGet(ResultSet rs, int position, SharedSessionContractImplementor session, Object owner) throws SQLException {
        val str = rs.getString(position);
        return objectMapper.readValue(str, Object.class);
    }

    @Override
    @SneakyThrows
    public void nullSafeSet(PreparedStatement st, Variable value, int index, SharedSessionContractImplementor session) throws SQLException {
        if (value == null) {
            st.setNull(index, Types.VARCHAR);
        }
        st.setString(index, objectMapper.writeValueAsString(value));
    }

    @Override
    public Variable deepCopy(Variable value) {
        return value.deepCopy();
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public Serializable disassemble(Variable value) {
        return null;
    }

    @Override
    public Variable assemble(Serializable cached, Object owner) {
        return null;
    }
}
