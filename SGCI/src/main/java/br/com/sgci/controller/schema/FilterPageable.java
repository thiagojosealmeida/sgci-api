package br.com.sgci.controller.schema;

import org.springframework.data.domain.Sort.Direction;

import jakarta.validation.constraints.NotNull;

public class FilterPageable
{
	@NotNull
    private Integer page = 0;

    @NotNull
    private Integer size = 10;

    private Direction direction = Direction.ASC;

    private String ordenarPor = "id";
    
	public Integer getPage() {
		return page;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public String getOrdenarPor() {
		return ordenarPor;
	}

	public void setOrdenarPor(String ordenarPor) {
		this.ordenarPor = ordenarPor;
	}

	public void setPage(Integer page) {
		this.page = page;
	}
}
