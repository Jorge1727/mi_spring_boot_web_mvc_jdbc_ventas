package org.iesvdm.dao;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

import org.iesvdm.modelo.Comercial;
import org.iesvdm.dto.ComercialDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

//Anotación lombok para logging (traza) de la aplicación
@Slf4j
@Repository
//Utilizo lombok para generar el constructor
@AllArgsConstructor
public class ComercialDAOImpl implements ComercialDAO {

	//JdbcTemplate se inyecta por el constructor de la clase automáticamente
	@Autowired
	private JdbcTemplate jdbcTemplate;



	@Override
	public void create(Comercial comercial) {

		String sqlInsert = """
							INSERT INTO comercial (nombre, apellido1, apellido2, comisión) 
							VALUES  (     ?,         ?,         ?,       ?)
						   """;

		KeyHolder keyHolder = new GeneratedKeyHolder();
		//Con recuperación de id generado
		int rows = jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(sqlInsert, new String[] { "id" });
			int idx = 1;
			ps.setString(idx++, comercial.getNombre());
			ps.setString(idx++, comercial.getApellido1());
			ps.setString(idx++, comercial.getApellido2());
			ps.setBigDecimal(idx++, comercial.getComision());
			return ps;
		},keyHolder);

		comercial.setId(keyHolder.getKey().intValue());
	}

	@Override
	public List<Comercial> getAll() {
		
		List<Comercial> listComercial = jdbcTemplate.query(
                "SELECT * FROM comercial",
                (rs, rowNum) -> new Comercial(rs.getInt("id"), 
                							  rs.getString("nombre"), 
                							  rs.getString("apellido1"),
                							  rs.getString("apellido2"),
                							  rs.getBigDecimal("comisión"))
                						 	
        );
		
		log.info("Devueltos {} registros.", listComercial.size());
		
        return listComercial;
	}

	@Override
	public Optional<Comercial> find(int id) {

		Comercial fab = jdbcTemplate.queryForObject("SELECT * FROM comercial WHERE id = ?"
				, (rs, rowNum) -> new Comercial(rs.getInt("id"),
						rs.getString("nombre"),
						rs.getString("apellido1"),
						rs.getString("apellido2"),
						rs.getBigDecimal("comisión"))
				, id);

		if (fab != null) {
			return Optional.of(fab);}
		else {
			log.info("Cliente no encontrado.");
			return Optional.empty(); }
	}

	@Override
	public void update(Comercial comercial) {

		int rows  = jdbcTemplate.update("""
											UPDATE comercial SET nombre = ?,apellido1 = ?, apellido2 = ?, comisión = ? WHERE id = ? 
											""",
				comercial.getNombre(),
				comercial.getApellido1(),
				comercial.getApellido2(),
				comercial.getComision(),
				comercial.getId());

		log.info("Update de comercial con {} registros actualizados.", rows);
	}

	@Override
	public void delete(long id) {

		int rows  = jdbcTemplate.update("DELETE FROM comercial WHERE id = ?", id);

		log.info("Delete de comercial con {} registros eliminados.", rows);
	}
	@Override
	public ComercialDTO comercialStats(int idComercial) {

		Optional<Comercial> comercial = find(idComercial);

		BigDecimal minimo = this.jdbcTemplate.queryForObject(
				"SELECT MIN(total) FROM PEDIDO WHERE id_comercial = ?",
				BigDecimal.class, idComercial);

		BigDecimal maximo = this.jdbcTemplate.queryForObject(
				"SELECT MAX(total) FROM PEDIDO WHERE id_comercial = ?",
				BigDecimal.class, idComercial);

		BigDecimal media = this.jdbcTemplate.queryForObject(
				"SELECT AVG(total) FROM PEDIDO WHERE id_comercial = ?",
				BigDecimal.class, idComercial);

		BigDecimal total = this.jdbcTemplate.queryForObject(
				"SELECT SUM(total) FROM PEDIDO WHERE id_comercial = ?",
				BigDecimal.class, idComercial);

		return new ComercialDTO(comercial.get().getNombre(), total.setScale(2, RoundingMode.HALF_UP), media.setScale(2, RoundingMode.HALF_UP), maximo.setScale(2, RoundingMode.HALF_UP), minimo.setScale(2, RoundingMode.HALF_UP));
	}



}
