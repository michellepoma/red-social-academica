package red_social_academica.red_social_academica.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.assertj.core.api.Assertions.assertThat;

class InvitationTest {

    @Test
    void testAcceptAddsUsersAsFriends() {
        User sender = User.builder().username("juan").name("Juan").lastName("Perez").email("juan@ex.com").ru("1234567")
                .password("123456").build();
        User receiver = User.builder().username("ana").name("Ana").lastName("Lopez").email("ana@ex.com").ru("7654321")
                .password("654321").build();
        Invitation invitation = new Invitation(sender, receiver);

        invitation.accept();

        assertThat(sender.getFriends()).contains(receiver);
        assertThat(receiver.getFriends()).contains(sender);
        assertThat(sender.getAuxFriends()).contains(receiver);
        assertThat(receiver.getAuxFriends()).contains(sender);
    }

    @Test
    void testUnlinkRemovesReferences() {
        User sender = User.builder().username("juan").name("Juan").lastName("Perez").email("juan@ex.com").ru("1234567")
                .password("123456").build();
        User receiver = User.builder().username("ana").name("Ana").lastName("Lopez").email("ana@ex.com").ru("7654321")
                .password("654321").build();
        Invitation invitation = new Invitation(sender, receiver);

        invitation.unlink();

        assertThat(invitation.getSender()).isNull();
        assertThat(invitation.getReceiver()).isNull();
    }

    @Test
    void testEsDelUsuario() {
        User sender = User.builder().username("juan").name("Juan").lastName("Perez").email("juan@ex.com").ru("1234567")
                .password("123456").build();
        User receiver = User.builder().username("ana").name("Ana").lastName("Lopez").email("ana@ex.com").ru("7654321")
                .password("654321").build();
        Invitation invitation = new Invitation(sender, receiver);

        assertThat(invitation.esDelUsuario("juan")).isTrue();
        assertThat(invitation.esDelUsuario("ana")).isTrue();
        assertThat(invitation.esDelUsuario("otro")).isFalse();
    }
}