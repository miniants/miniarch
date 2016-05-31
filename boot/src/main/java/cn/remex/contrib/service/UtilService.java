package cn.remex.contrib.service;

import cn.remex.contrib.appbeans.UtilBsCvo;
import cn.remex.contrib.appbeans.UtilBsRvo;
import cn.remex.core.reflect.ReflectUtil;
import cn.remex.core.util.FileHelper;
import cn.remex.db.ContainerFactory;
import cn.remex.db.model.cert.AuthUser;
import cn.remex.db.rsql.transactional.RsqlTransaction;
import cn.remex.web.service.BsException;
import cn.remex.web.service.BsRvo;
import cn.remex.web.service.BusinessService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author liuhengyang
 * @date 2015-8-29 下午6:20:59
 * @version 版本号码
 * @TODO 描述
 */
@BusinessService
public class UtilService {
	@BusinessService
	@RsqlTransaction()
	public Object test() throws SQLException {
//		DataSource dataSource = RemexApplication.getBean("dataSource");
//		Connection conn = dataSource.getConnection();
//		return conn.isValid(100);

		AuthUser au = new AuthUser();
//		DbRvo student =  ContainerFactory.getSession().createDbCvo(AuthUser.class)
//				.filterBy(AuthUser::getUsername, inSubSelect,Student.class, a->a.withColumns(Student::getStuNo).filterBy(Student::getStuNo,eq,"0221013"))
//			.ready().query();
		return au;

//		DbRvo dbRvo = ContainerFactory.createDbCvo(StuCourseSchedule.class)
//				.withColumns(StuCourseSchedule::getClassInfo, SqlColumn::groupBy)
//				.withColumns(StuCourseSchedule::getId, id -> id.aggregateBy(COUNT))
//				.filterBy(StuCourseSchedule::getPodium, in, TeachEvalPodium.class, subSqlColumn->subSqlColumn.filterBy(TeachEvalPodium::getPodium,eq,""))
//				.ready().query();

//		DbRvo dbRvo = ContainerFactory.createDbCvo(StuCourseSchedule.class).rowCount(1)
//				.withModel(StuCourseSchedule::getStudent, stu->stu.withModel(Student::getClassInfo, SqlColumn::groupBy))
////				.withColumns(StuCourseSchedule::getClassInfo, SqlColumn::groupBy)
//				.withColumns(StuCourseSchedule::getId, id -> id.aggregateBy(COUNT)).ready().query();

//		return dbRvo.getMapRows();
	}
	@BusinessService
	@RsqlTransaction(useBatchUpdate = true)
	public Object testBatch() throws SQLException {
		int count = 100010;
		for(int i=0;i<count;i++) {
			AuthUser au = new AuthUser();
			au.setUsername("name"+i);
			ContainerFactory.getSession().store(au);
		}
		return count;
	}
	@BusinessService
	@RsqlTransaction(useBatchUpdate = false)
	public Object testNotBatch() throws SQLException {
		int count = 100010;
		for(int i=0;i<count;i++) {
			AuthUser au = new AuthUser();
			au.setUsername("nameb"+i);
			ContainerFactory.getSession().store(au);
		}
		return count;
	}

	// /menu Type 对应的功能方法。
	@BusinessService
	public BsRvo uploadFile(UtilBsCvo bsCvo) {
		Object[] files = (Object[]) bsCvo.getParams().get("files");
		String contextPath = bsCvo.getContextPath();

		List<String> filenames = new ArrayList<>();
		try {
			for (Object part : files) {
				int pre = (int) System.currentTimeMillis();
				// 拿到输出流，同时重命名上传的文件
				FileOutputStream os;
				String uri = "data"+File.separatorChar + new Date().getTime() + ReflectUtil.invokeGetter("originalFilename", part);
				String path = contextPath + uri ;
				os = new FileOutputStream(FileHelper.createFile(path));
				// 拿到上传文件的输入流
				FileInputStream in = (FileInputStream) ReflectUtil
						.invokeGetter("inputStream", part);

				// 以写字节的方式写文件
				int b = 0;
				while ((b = in.read()) != -1) {
					os.write(b);
				}
				os.flush();
				os.close();
				in.close();
				int finaltime = (int) System.currentTimeMillis();
				System.out.println(finaltime - pre);
				filenames.add(uri.replace(File.separatorChar, '/'));
			}
		} catch (Exception e) {
			throw new BsException("文件上传失败!",e);
		}

		UtilBsRvo bsRvo = new UtilBsRvo();
		bsRvo.setFilenames(filenames);

		return bsRvo;
	}
}
