package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import data.GymDatabase;

/**
 * Main window shown after login. Hosts the menu bar, a collapsible navigation
 * sidebar, and the swappable page area where each module is displayed.
 */
public class DashboardFrame extends JFrame {
    private static final String TITLE_BASE = "Gym Management System - ";
    private static final int SIDEBAR_EXPANDED_WIDTH = 240;
    private static final int SIDEBAR_COLLAPSED_WIDTH = 72;
    private static final int TILE = 44; // square size for collapsed module icons

    private static final Color WHITE_ICON = new Color(0xEC, 0xEC, 0xF0);
    private static final Color EXIT_RED = new Color(0xC0, 0x3A, 0x30);

    private final GymDatabase database;

    private JPanel pageHolder;          // centre area where module panels appear
    private JPanel sidebar;             // left navigation rail
    private JLabel sidebarTitle;
    private JButton sidebarToggle;
    private final List<NavButton> navButtons = new ArrayList<>();

    private boolean sidebarExpanded = true;
    private boolean onWelcome = true;

    public DashboardFrame(GymDatabase database) {
        this.database = database;

        setTitle(TITLE_BASE + "Dashboard");
        setIconImages(AppStyle.createAppIcons()); // gold dumbbell taskbar icon
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 750);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // open maximized (rubric requirement)

        // Leaving maximized (restore button or dragging) snaps to a clean size.
        addWindowStateListener(e -> {
            if (e.getNewState() == java.awt.Frame.NORMAL) {
                setSize(1100, 750);
                setLocationRelativeTo(null);
            }
        });

        setJMenuBar(buildMenuBar());
        setLayout(new BorderLayout());

        sidebar = buildSidebar();
        pageHolder = new JPanel(new BorderLayout());
        pageHolder.setBackground(AppStyle.BACKGROUND_COLOR);

        add(sidebar, BorderLayout.WEST);
        add(pageHolder, BorderLayout.CENTER);

        showWelcomePage();
    }

    // ---- Menu bar (satisfies the "menus" GUI component) ----
    private JMenuBar buildMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu modules = new JMenu("Modules");
        modules.add(menuItem("Member Management", this::showMemberPanel));
        modules.add(menuItem("Trainer Management", this::showTrainerPanel));
        modules.add(menuItem("Membership Plans", this::showMembershipPanel));
        modules.add(menuItem("Workout Schedule", this::showWorkoutSchedulePanel));
        modules.add(menuItem("Financing", this::showFinancingPanel));

        JMenu view = new JMenu("View");
        view.add(menuItem("Dashboard Home", this::showWelcomePage));
        view.add(menuItem("Toggle Sidebar", this::toggleSidebar));

        JMenu account = new JMenu("Account");
        account.add(menuItem("Log Out", this::logout));
        account.add(menuItem("Exit", this::confirmExit));

        menuBar.add(modules);
        menuBar.add(view);
        menuBar.add(account);
        return menuBar;
    }

    private JMenuItem menuItem(String text, Runnable action) {
        JMenuItem item = new JMenuItem(text);
        item.addActionListener(e -> action.run());
        return item;
    }

    // ---- Sidebar ----
    private JPanel buildSidebar() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Opaque while pinned (expanded), translucent while unpinned (collapsed)
                g2.setColor(sidebarExpanded ? AppStyle.SIDEBAR_OPAQUE_COLOR : AppStyle.SIDEBAR_COLOR);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(SIDEBAR_EXPANDED_WIDTH, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 12, 10));

        // Pin / unpin toggle ("«" pinned at the right, "•••" when minimized)
        sidebarToggle = new JButton("«");
        sidebarToggle.setFont(AppStyle.font(Font.BOLD, 18));
        sidebarToggle.setForeground(AppStyle.GOLD_COLOR);
        sidebarToggle.setContentAreaFilled(false);
        sidebarToggle.setBorderPainted(false);
        sidebarToggle.setFocusPainted(false);
        sidebarToggle.setMargin(new Insets(0, 0, 0, 0));
        sidebarToggle.setMaximumSize(new Dimension(Integer.MAX_VALUE, 22));
        sidebarToggle.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        sidebarToggle.setCursor(new Cursor(Cursor.HAND_CURSOR));
        sidebarToggle.setToolTipText("Pin / unpin sidebar");
        sidebarToggle.addActionListener(e -> toggleSidebar());

        sidebarTitle = new JLabel("Gym Management");
        sidebarTitle.setFont(AppStyle.font(Font.BOLD, 22));
        sidebarTitle.setForeground(AppStyle.GOLD_COLOR);
        sidebarTitle.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        sidebarTitle.setHorizontalAlignment(SwingConstants.LEFT);
        sidebarTitle.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        sidebarTitle.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 0));

        panel.add(sidebarToggle);
        panel.add(Box.createVerticalStrut(8));
        panel.add(sidebarTitle);
        panel.add(Box.createVerticalStrut(20));

        addNav(panel, NavIcon.Kind.MEMBER, "Member Management", NavRole.MODULE, WHITE_ICON, this::showMemberPanel);
        panel.add(Box.createVerticalStrut(10));
        addNav(panel, NavIcon.Kind.TRAINER, "Trainer Management", NavRole.MODULE, WHITE_ICON, this::showTrainerPanel);
        panel.add(Box.createVerticalStrut(10));
        addNav(panel, NavIcon.Kind.MEMBERSHIP, "Membership Plans", NavRole.MODULE, WHITE_ICON, this::showMembershipPanel);
        panel.add(Box.createVerticalStrut(10));
        addNav(panel, NavIcon.Kind.WORKOUT, "Workout Schedule", NavRole.MODULE, WHITE_ICON, this::showWorkoutSchedulePanel);
        panel.add(Box.createVerticalStrut(10));
        addNav(panel, NavIcon.Kind.FINANCING, "Financing", NavRole.MODULE, WHITE_ICON, this::showFinancingPanel);

        panel.add(Box.createVerticalGlue()); // pushes the buttons below to the bottom

        addNav(panel, NavIcon.Kind.MONITOR, "Dashboard", NavRole.BOTTOM, WHITE_ICON, this::showWelcomePage);
        panel.add(Box.createVerticalStrut(8));
        addNav(panel, NavIcon.Kind.LOGOUT, "Log Out", NavRole.BOTTOM, AppStyle.GOLD_COLOR, this::logout);
        panel.add(Box.createVerticalStrut(8));
        addNav(panel, NavIcon.Kind.EXIT, "Exit", NavRole.EXIT, EXIT_RED, this::confirmExit);

        return panel;
    }

    private void addNav(JPanel panel, NavIcon.Kind kind, String label, NavRole role,
            Color accent, Runnable action) {
        NavButton button = new NavButton(kind, label, role, accent, action);
        navButtons.add(button);
        panel.add(button);
    }

    private void toggleSidebar() {
        // The sidebar stays pinned (expanded) on the dashboard home.
        if (onWelcome) {
            return;
        }
        setSidebarExpanded(!sidebarExpanded);
    }

    private void setSidebarExpanded(boolean expanded) {
        sidebarExpanded = expanded;
        sidebar.setPreferredSize(new Dimension(
                expanded ? SIDEBAR_EXPANDED_WIDTH : SIDEBAR_COLLAPSED_WIDTH, 0));

        sidebarTitle.setVisible(expanded);
        sidebarToggle.setText(expanded ? "«" : "•••");
        // Pinned: arrow sits at the right edge; minimized: centered dots.
        sidebarToggle.setHorizontalAlignment(expanded ? SwingConstants.RIGHT : SwingConstants.CENTER);

        for (NavButton button : navButtons) {
            button.setExpanded(expanded);
        }
        sidebar.revalidate();
        sidebar.repaint();
        getContentPane().revalidate(); // let the page area reflow into the freed space
    }

    private void setActiveModule(NavIcon.Kind kind) {
        for (NavButton button : navButtons) {
            button.setActive(button.role == NavRole.MODULE && button.kind == kind);
        }
    }

    // ---- Pages ----
    private void setPage(JComponent page) {
        pageHolder.removeAll();
        pageHolder.add(page, BorderLayout.CENTER);
        pageHolder.revalidate();
        pageHolder.repaint();
    }

    private void showWelcomePage() {
        onWelcome = true;
        setSidebarExpanded(true);          // pinned & opaque on the home screen
        sidebarToggle.setEnabled(false);
        setActiveModule(null);
        setTitle(TITLE_BASE + "Dashboard");

        BackgroundPanel background = new BackgroundPanel(new GridBagLayout());

        JPanel hero = new JPanel();
        hero.setOpaque(false);
        hero.setLayout(new BoxLayout(hero, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Welcome to Gym Management");
        title.setFont(AppStyle.font(Font.BOLD, 34));
        title.setForeground(AppStyle.GOLD_COLOR);
        title.setAlignmentX(JComponent.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Choose a module from the sidebar or the menu bar to begin");
        subtitle.setFont(AppStyle.LABEL_FONT);
        subtitle.setForeground(AppStyle.TEXT_COLOR);
        subtitle.setAlignmentX(JComponent.CENTER_ALIGNMENT);

        hero.add(title);
        hero.add(Box.createVerticalStrut(10));
        hero.add(subtitle);

        background.add(hero);
        setPage(background);
    }

    private void openModule(NavIcon.Kind kind, String title, JComponent panel) {
        onWelcome = false;
        sidebarToggle.setEnabled(true);
        setActiveModule(kind);
        setTitle(TITLE_BASE + title);
        setPage(panel);
    }

    private void confirmExit() {
        if (ConfirmDialog.show(this, "Are you sure?")) {
            System.exit(0);
        }
    }

    private void logout() {
        new LoginFrame(database).setVisible(true); // back to ui.LoginFrame
        dispose();
    }

    // Each show* method builds the matching screen and drops it into the page area.
    // The second argument (this::showWelcomePage) is the "Back" action each panel calls.
    private void showMemberPanel() {
        openModule(NavIcon.Kind.MEMBER, "Member Management",
                new MemberPanel(database, this::showWelcomePage)); // -> ui.MemberPanel
    }

    private void showTrainerPanel() {
        openModule(NavIcon.Kind.TRAINER, "Trainer Management",
                new TrainerPanel(database, this::showWelcomePage)); // -> ui.TrainerPanel
    }

    private void showMembershipPanel() {
        openModule(NavIcon.Kind.MEMBERSHIP, "Membership Plans",
                new MembershipPanel(this::showWelcomePage)); // -> ui.MembershipPanel
    }

    private void showWorkoutSchedulePanel() {
        openModule(NavIcon.Kind.WORKOUT, "Workout Schedule",
                new WorkoutSchedulePanel(database, this::showWelcomePage)); // -> ui.WorkoutSchedulePanel
    }

    private void showFinancingPanel() {
        openModule(NavIcon.Kind.FINANCING, "Financing",
                new FinancingPanel(database, this::showWelcomePage)); // -> ui.FinancingPanel
    }

    private enum NavRole { MODULE, BOTTOM, EXIT }

    /**
     * A borderless sidebar entry. When highlighted (a module's panel is active,
     * or any entry is hovered) a coloured underline is drawn beneath the icon —
     * gold for modules and Log Out, white for Dashboard, red for Exit. Module
     * icons become centered squares when the rail is collapsed.
     */
    private class NavButton extends JButton {
        private final NavIcon.Kind kind;
        private final NavRole role;
        private final String label;
        private final NavIcon icon;
        private final Color accent;
        private boolean active;
        private boolean expanded = true;

        NavButton(NavIcon.Kind kind, String label, NavRole role, Color accent, Runnable action) {
            this.kind = kind;
            this.label = label;
            this.role = role;
            this.accent = accent;
            this.icon = new NavIcon(kind, accent);

            setIcon(icon);
            setFont(AppStyle.font(Font.PLAIN, 15));
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            addActionListener(e -> action.run());
            setExpanded(true);
        }

        void setActive(boolean active) {
            this.active = active;
            repaint();
        }

        void setExpanded(boolean expanded) {
            this.expanded = expanded;
            setText(expanded ? label : "");
            setIconTextGap(expanded ? 14 : 0);
            if (!expanded && role == NavRole.MODULE) {
                setHorizontalAlignment(CENTER);
                setAlignmentX(CENTER_ALIGNMENT);     // square tile, centered in the rail
                setBorder(BorderFactory.createEmptyBorder());
                lockSize(new Dimension(TILE, TILE));
            } else if (!expanded) {
                setHorizontalAlignment(CENTER);      // bottom buttons stay rectangles
                setAlignmentX(CENTER_ALIGNMENT);
                setBorder(BorderFactory.createEmptyBorder());
                flexSize(44);
            } else {
                setHorizontalAlignment(LEFT);
                setAlignmentX(CENTER_ALIGNMENT);
                setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 12));
                flexSize(46);
            }
            setToolTipText(expanded ? null : label);
        }

        private void lockSize(Dimension d) {
            setMinimumSize(d);
            setPreferredSize(d);
            setMaximumSize(d);
        }

        private void flexSize(int height) {
            setMinimumSize(new Dimension(0, height));
            setPreferredSize(new Dimension(SIDEBAR_EXPANDED_WIDTH, height));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, height));
        }

        @Override
        protected void paintComponent(Graphics g) {
            boolean hot = getModel().isRollover() || getModel().isPressed();
            boolean highlighted = (role == NavRole.MODULE) ? (active || hot) : hot;

            // Icon/text colour: modules go gold when highlighted; others keep accent.
            Color content = (role == NavRole.MODULE && highlighted) ? AppStyle.GOLD_COLOR
                    : (role == NavRole.MODULE ? WHITE_ICON : accent);
            icon.setColor(content);
            setForeground(content);

            super.paintComponent(g); // draws the icon and label

            if (highlighted) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color line = (role == NavRole.MODULE) ? AppStyle.GOLD_COLOR : accent;
                g2.setColor(line);
                int iconW = 24;
                int lx = expanded ? getInsets().left : (getWidth() - iconW) / 2;
                int ly = getHeight() - 5;
                g2.fillRoundRect(lx, ly, iconW, 3, 2, 2);
                g2.dispose();
            }
        }
    }
}
