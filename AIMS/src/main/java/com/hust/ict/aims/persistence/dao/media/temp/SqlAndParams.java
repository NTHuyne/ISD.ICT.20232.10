package com.hust.ict.aims.persistence.dao.media.temp;

import java.sql.PreparedStatement;
import java.util.function.Consumer;
import java.util.function.Function;

public class SqlAndParams<T> {
	private final String sql;
	private final Function<PreparedStatement, Consumer<T>> parameterizer;
	
	public SqlAndParams(String sql, Function<PreparedStatement, Consumer<T>> parameterizer) {
		super();
		this.sql = sql;
		this.parameterizer = parameterizer;
	}

	public String getSql() {
		return sql;
	}

	public Function<PreparedStatement, Consumer<T>> getParameterizer() {
		return parameterizer;
	}
}
