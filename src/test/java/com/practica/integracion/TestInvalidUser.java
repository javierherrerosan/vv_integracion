package com.practica.integracion;

import com.practica.integracion.DAO.AuthDAO;
import com.practica.integracion.DAO.GenericDAO;
import com.practica.integracion.DAO.User;
import com.practica.integracion.manager.SystemManager;
import com.practica.integracion.manager.SystemManagerException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.naming.OperationNotSupportedException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TestInvalidUser {
	@Mock
	private static AuthDAO mockAuthDao;
	@Mock
	private static GenericDAO mockGenericDao;
	@Test
	public void testStartRemoteSystemWithInvalidUserAndValidSystem() throws OperationNotSupportedException, SystemManagerException {
		User InvalidUser = new User("1","Ana","Lopez","Madrid", new ArrayList<Object>(Arrays.asList(1, 2)));
		when(mockAuthDao.getAuthData(InvalidUser.getId())).thenReturn(null);

		String validId = "12345"; // id valido de sistema
		ArrayList<Object> lista = new ArrayList<>(Arrays.asList("uno", "dos"));
		when(mockGenericDao.getSomeData(null, "where id=" + validId)).thenThrow(javax.naming.OperationNotSupportedException.class);

		InOrder ordered = inOrder(mockAuthDao, mockGenericDao);

		SystemManager manager = new SystemManager(mockAuthDao, mockGenericDao);
		assertThrows(com.practica.integracion.manager.SystemManagerException.class, ()->{
			manager.startRemoteSystem(InvalidUser.getId(), validId);
		});
		ordered.verify(mockAuthDao).getAuthData(InvalidUser.getId());
		ordered.verify(mockGenericDao).getSomeData(null, "where id=" + validId);
	}
	@Test
	public void testStartRemoteSystemWithInvalidUserAndSystem() throws SystemManagerException, OperationNotSupportedException {
		User InvalidUser = new User("1","Ana","Lopez","Madrid", new ArrayList<Object>(Arrays.asList(1, 2)));
		when(mockAuthDao.getAuthData(InvalidUser.getId())).thenReturn(null);
		String InvalidvalidId = "12345"; // id valido de sistema

		ArrayList<Object> lista = new ArrayList<>(Arrays.asList("uno", "dos"));
		when(mockGenericDao.getSomeData(null, "where id=" + InvalidvalidId)).thenReturn(null);

		InOrder ordered = inOrder(mockAuthDao, mockGenericDao);
		SystemManager manager = new SystemManager(mockAuthDao, mockGenericDao);
		Collection<Object> retorno = manager.startRemoteSystem(InvalidUser.getId(), InvalidvalidId);

		assertNull(retorno);
		ordered.verify(mockAuthDao).getAuthData(InvalidUser.getId());
		ordered.verify(mockGenericDao).getSomeData(null, "where id=" + InvalidvalidId);
	}
	
	 @Test
    public void stopRemoteSystemWithInvalidUserAndSystem() throws SystemManagerException, OperationNotSupportedException {


        User invalidUser = new User("1","Ana","Lopez","Madrid", new ArrayList<Object>(Arrays.asList(1, 2)));
        when(mockAuthDao.getAuthData(invalidUser.getId())).thenReturn(null);
        String validId = "12345"; // id valido de sistema
        ArrayList<Object> lista = new ArrayList<>(Arrays.asList("uno", "dos"));
        when(mockGenericDao.getSomeData(null, "where id=" + validId)).thenThrow(new OperationNotSupportedException());
        InOrder ordered = inOrder(mockAuthDao, mockGenericDao);
        // instanciamos el manager con los mock creados
        SystemManager manager = new SystemManager(mockAuthDao, mockGenericDao);
        // llamada al api a probar
        assertThrows(com.practica.integracion.manager.SystemManagerException.class, ()->{
           manager.stopRemoteSystem(invalidUser.getId(), validId);
        });

        // vemos si se ejecutan las llamadas a los dao, y en el orden correcto
        ordered.verify(mockAuthDao).getAuthData(invalidUser.getId());
        ordered.verify(mockGenericDao).getSomeData(null, "where id=" + validId);

    }
	
	@Test
    public void stopRemoteSystemWithInvalidUserAndInvalidSystem() throws SystemManagerException, OperationNotSupportedException {


        User invalidUser = new User("1","Ana","Lopez","Madrid", new ArrayList<Object>(Arrays.asList(1, 2)));
        when(mockAuthDao.getAuthData(invalidUser.getId())).thenReturn(null);
        String invalidId = "12345"; // id valido de sistema
        ArrayList<Object> lista = new ArrayList<>(Arrays.asList("uno", "dos"));
        when(mockGenericDao.getSomeData(null, "where id=" + invalidId)).thenReturn(null);
        InOrder ordered = inOrder(mockAuthDao, mockGenericDao);
        // instanciamos el manager con los mock creados
        SystemManager manager = new SystemManager(mockAuthDao, mockGenericDao);
        // llamada al api a probar
        Collection<Object> retorno = manager.stopRemoteSystem(invalidUser.getId(), invalidId);
        assertNull(retorno);
        // vemos si se ejecutan las llamadas a los dao, y en el orden correcto
        ordered.verify(mockAuthDao).getAuthData(invalidUser.getId());
        ordered.verify(mockGenericDao).getSomeData(null, "where id=" + invalidId);

    }
	
}
