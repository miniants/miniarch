/**
 * 
 */
package cn.remex.db.sql.statement;

/**
 * @author Hengyang Liu
 * @since 2012-4-17
 *
 */
public class Select {
	SelectColumnFragment selectColumn;
	SelectFromFragment selectFrom;
	SelectGroupFragment selectGroup;
	SelectHavingFragment selectHaving;
	SelectJoinFragment selectJoin;
	SelectOrderFragment selectOrder;
	SelectWhereFragment selectWhere;
}
