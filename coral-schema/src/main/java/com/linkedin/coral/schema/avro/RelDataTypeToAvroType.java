/**
 * Copyright 2019 LinkedIn Corporation. All rights reserved.
 * Licensed under the BSD-2 Clause license.
 * See LICENSE in the project root for license information.
 */
package com.linkedin.coral.schema.avro;

import com.linkedin.coral.com.google.common.base.Preconditions;

import javax.annotation.Nonnull;

import org.apache.avro.Schema;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.sql.type.ArraySqlType;
import org.apache.calcite.sql.type.BasicSqlType;


/**
 * This class provides RelDataType to avro data type mapping
 * <p>
 * relDataTypeToAvroType is the main API
 */
class RelDataTypeToAvroType {
  // private constructor for utility class
  private RelDataTypeToAvroType() {
  }

  /**
   * This method converts RelDataType to avro data type
   *
   * @param relDataType
   * @return Schema of Avro data type corresponding to input RelDataType
   */
  static Schema relDataTypeToAvroType(@Nonnull RelDataType relDataType) {
    Preconditions.checkNotNull(relDataType);

    if (relDataType instanceof BasicSqlType) {
      return basicSqlTypeToAvroType((BasicSqlType) relDataType);
    }

    if (relDataType instanceof ArraySqlType) {
      return Schema.createArray(relDataTypeToAvroType(relDataType.getComponentType()));
    }

    // TODO: support more RelDataType if necessary
    // For example: MultisetSqlType, RelRecordType, DynamicRecordType, MapSqlType

    // TODO: improve logging
    throw new UnsupportedOperationException("Unsupported RelDataType: " + relDataType.toString());
  }

  private static Schema basicSqlTypeToAvroType(BasicSqlType relType) {
    switch (relType.getSqlTypeName()) {
      case BOOLEAN:
        return Schema.create(Schema.Type.BOOLEAN);
      case TINYINT:
      case INTEGER:
        return Schema.create(Schema.Type.INT);
      case BIGINT:
        return Schema.create(Schema.Type.LONG);
      case FLOAT:
        return Schema.create(Schema.Type.FLOAT);
      case DOUBLE:
        return Schema.create(Schema.Type.DOUBLE);
      case VARCHAR:
      case CHAR:
        return Schema.create(Schema.Type.STRING);
      case BINARY:
        return Schema.create(Schema.Type.BYTES);
      case NULL:
        return Schema.create(Schema.Type.NULL);
      case ANY:
        return Schema.create(Schema.Type.BYTES);
      default:
        throw new UnsupportedOperationException(relType.getSqlTypeName() + " is not supported.");
    }
  }
}
